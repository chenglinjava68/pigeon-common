package cn.yiidii.pigeon.common.strategy.component;

import cn.yiidii.pigeon.common.strategy.annotation.HandlerType;
import cn.yiidii.pigeon.common.strategy.exception.DuplicateBizCodeException;
import cn.yiidii.pigeon.common.strategy.exception.StrategyExceptionCode;
import cn.yiidii.pigeon.common.strategy.prop.StrategyProperties;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * HandlerContext Bean的后置处理器
 *
 * @author YiiDii Wang
 * @create 2021-06-06 22:21
 */
@Slf4j
@ConditionalOnClass(StrategyProperties.class)
@AllArgsConstructor
public class HandlerContextBeanPostProcessor implements BeanPostProcessor {

    private static final String RESOURCE_PATTERN = "/**/*" + ClassUtils.CLASS_FILE_SUFFIX;
    private final StrategyProperties strategyProperties;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof HandlerContext) {
            HandlerContext handlerContext = (HandlerContext) bean;
            handlerContextBeanPost(handlerContext);
        }
        return bean;

    }

    @SneakyThrows
    public void handlerContextBeanPost(HandlerContext handlerContext) {
        Map<String, String> handlerMap = Maps.newConcurrentMap();
        // 获取指定报下的所有类
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        MetadataReaderFactory metaReader = new CachingMetadataReaderFactory();
        List<Class<?>> list = Lists.newArrayList();
        final String basePackage = strategyProperties.getBasePackage();
        String classPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + ClassUtils.convertClassNameToResourcePath(basePackage) + RESOURCE_PATTERN;
        log.info("策略业务组件扫描basePackage: {}; classPath: {}", basePackage, classPath);
        Resource[] resources = resolver.getResources(classPath);
        for (Resource resource : resources) {
            MetadataReader reader = metaReader.getMetadataReader(resource);
            String className = reader.getClassMetadata().getClassName();
            Class<?> clazz = Class.forName(className);
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
        handlerContext.setHandlerMap(handlerMap);
        log.info("扫描到策略业务handler: {}", JSONObject.toJSON(handlerMap.keySet()));
    }
}
