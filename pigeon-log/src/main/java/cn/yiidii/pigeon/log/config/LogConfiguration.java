package cn.yiidii.pigeon.log.config;

import cn.yiidii.pigeon.log.event.LogListener;
import cn.yiidii.pigeon.rbac.api.feign.SysLogFeign;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.annotation.PostConstruct;

/**
 * 日志配置
 *
 * @author: YiiDii Wang
 * @create: 2021-04-12 22:23
 */
@Slf4j
@EnableAsync
@Configuration
@RequiredArgsConstructor
@ConditionalOnWebApplication
public class LogConfiguration {

    private final SysLogFeign sysLogFeign;

    @Bean
    public LogListener sysLogListener() {
        return new LogListener(sysLogFeign);
    }

}
