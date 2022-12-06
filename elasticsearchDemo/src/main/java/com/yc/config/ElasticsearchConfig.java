package com.yc.config;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yucan
 * @date 2022/12/5 17:48
 */
@Configuration
public class ElasticsearchConfig {
    @Value("${es.host.address}")
    private String address;

    @Value("${es.host.port}")
    private Integer port;

    @Value("${es.host.userName}")
    private String userName;

    @Value("${es.host.password}")
    private String password;

    @Bean(destroyMethod = "close")
    public RestHighLevelClient client() {
        //凭证注册器
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();

        //注册
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(userName, password));

        RestClientBuilder builder = RestClient.builder(new HttpHost(address, port))
                .setHttpClientConfigCallback(httpAsyncClientBuilder ->
                        httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider));

        return new RestHighLevelClient(builder);
    }
}
