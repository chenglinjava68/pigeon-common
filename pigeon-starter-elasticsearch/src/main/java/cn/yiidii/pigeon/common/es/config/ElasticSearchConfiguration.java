package cn.yiidii.pigeon.common.es.config;

import cn.hutool.core.util.StrUtil;
import cn.yiidii.pigeon.common.es.prop.ElasticSearchProperties;
import com.sun.deploy.util.StringUtils;
import lombok.AllArgsConstructor;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

/**
 * @author YiiDii Wang
 * @create 2021-06-29 23:25
 */
@Configuration
@AllArgsConstructor
@EnableConfigurationProperties(ElasticSearchProperties.class)
public class ElasticSearchConfiguration extends AbstractElasticsearchConfiguration {

    private final ElasticSearchProperties elasticSearchProperties;

    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient() {
        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(StringUtils.join(elasticSearchProperties.getUris(), StrUtil.COMMA))
                .withBasicAuth(elasticSearchProperties.getUsername(), elasticSearchProperties.getPassword())
                .withSocketTimeout(elasticSearchProperties.getReadTimeout())
                .build();
        return RestClients.create(clientConfiguration).rest();
    }
}