package com.lsh.milvus.config;

import io.milvus.client.MilvusServiceClient;
import io.milvus.param.ConnectParam;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MilvusConfig {

    @Bean
    public MilvusServiceClient milvusClient(){
        return new MilvusServiceClient(
                ConnectParam.newBuilder()
                        //.withUri("http://192.168.153.100:19530")
                        .withUri("http://vm100:19530")
                        .withToken("root:Milvus")
                        .build()
        );
    }

}
