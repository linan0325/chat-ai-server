package com.nan.chatai.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClientBuilder;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.client5.http.impl.routing.DefaultProxyRoutePlanner;
import org.apache.hc.core5.http.HttpHost;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatGptHttpClientConfig {

    @Value("${chat-ai.proxy.host}")
    private String host;
    @Value("${chat-ai.proxy.port}")
    private Integer port;


    @Bean(name = "httpAsyncClient")
    public CloseableHttpAsyncClient httpAsyncClient(){

     if(host !=null && port !=null){
         HttpHost proxy = new HttpHost(host, port);
         DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
         return HttpAsyncClients.custom().setRoutePlanner(routePlanner).build();
     }
        return HttpAsyncClients.createHttp2Default();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
