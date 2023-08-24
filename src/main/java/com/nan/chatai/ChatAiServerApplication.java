package com.nan.chatai;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.nan.chatai.dao")
public class ChatAiServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatAiServerApplication.class, args);
    }

}
