package cn.yiidii.pigeon.common.strategy.prop;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * 策略模块属性
 *
 * @author YiiDii Wang
 * @create 2021-06-06 20:57
 */
@Data
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "pigeon.strategy")
public class StrategyProperties {

    private String classPath = "classpath*:cn/yiidii/pigeon/**/handler/**/*.class";

}
