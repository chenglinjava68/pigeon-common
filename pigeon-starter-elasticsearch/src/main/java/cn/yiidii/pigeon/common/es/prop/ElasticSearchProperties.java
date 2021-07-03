package cn.yiidii.pigeon.common.es.prop;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author YiiDii Wang
 * @create 2021-06-29 23:23
 */
@Data
@ToString
@ConfigurationProperties(prefix = ElasticSearchProperties.ES_PREFIX)
public class ElasticSearchProperties {

    public static final String ES_PREFIX = "pigeon.es";

    private List<String> uris = new ArrayList<>(Collections.singletonList("http://localhost:9200"));
    private String username;
    private String password;
    private Duration connectionTimeout = Duration.ofSeconds(1L);
    private Duration readTimeout = Duration.ofSeconds(30L);

    @PostConstruct
    public void init() {
        System.out.println(this.toString());
    }
}