package cn.yiidii.pigeon.common.security.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * 忽略url配置
 *
 * @author: YiiDii Wang
 * @create: 2021-01-18 22:39
 */
@Data
@Slf4j
@ConfigurationProperties(prefix = "pigeon")
public class IgnoreUrlProperties {

    @PostConstruct
    public void init(){
        log.info("===== IgnoreUrlProperties init...");
    }

    List<String> ignoreUrls = new ArrayList<>();

}
