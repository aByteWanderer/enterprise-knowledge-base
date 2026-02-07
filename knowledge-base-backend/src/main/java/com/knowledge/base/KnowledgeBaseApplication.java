package com.knowledge.base;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.knowledge.base.mapper")
@EnableAsync
@EnableScheduling
public class KnowledgeBaseApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(KnowledgeBaseApplication.class, args);
    }
    
}
