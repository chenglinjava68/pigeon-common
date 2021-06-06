package cn.yiidii.pigeon.common.strategy.component;

import cn.yiidii.pigeon.common.strategy.annotation.HandlerType;
import cn.yiidii.pigeon.common.strategy.exception.DuplicateBizCodeException;
import cn.yiidii.pigeon.common.strategy.exception.StrategyExceptionCode;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.SneakyThrows;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author YiiDii Wang
 * @create 2021-06-06 12:16
 */
public class HandlerProcessor implements BeanFactoryPostProcessor {

    @SneakyThrows
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) {
        Map<String, String> handlerMap = Maps.newConcurrentMap();
        // 获取指定报下的所有类
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        MetadataReaderFactory metaReader = new CachingMetadataReaderFactory();
        List<Class<?>> list = Lists.newArrayList();
        Resource[] resources = resolver.getResources("classpath*:cn/yiidii/pigeon/**/handler/**/*.class");
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        for (Resource resource : resources) {
            MetadataReader reader = metaReader.getMetadataReader(resource);
            String className = reader.getClassMetadata().getClassName();
            Class<?> clazz = loader.loadClass(className);
            HandlerType annotation = clazz.getAnnotation(HandlerType.class);
            if (Objects.isNull(annotation)) {
                continue;
            }
            // 判断是一个类是不是抽象类
            boolean anAbstract = Modifier.isAbstract(clazz.getModifiers());
            if (!anAbstract) {
                list.add(clazz);
            }
        }
        for (Class<?> aClass : list) {
            Annotation[] annotations = aClass.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof HandlerType) {
                    String bizCode = ((HandlerType) annotation).bizCode();
                    String beanName = ((HandlerType) annotation).beanName();
                    if (handlerMap.containsKey(bizCode)) {
                        throw new DuplicateBizCodeException(StrategyExceptionCode.DUPLICATE_BIZ_CODE.getCode(), StrategyExceptionCode.DUPLICATE_BIZ_CODE.getMsg(), bizCode);
                    } else {
                        handlerMap.put(bizCode, beanName);
                    }
                }
            }
        }
        HandlerContext handleContext = new HandlerContext(handlerMap);
        // 将上下文添加到spring bean容器中
        configurableListableBeanFactory.registerSingleton(HandlerContext.class.getName(), handleContext);
    }
}
