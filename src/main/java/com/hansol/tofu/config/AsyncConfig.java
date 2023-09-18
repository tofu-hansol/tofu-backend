package com.hansol.tofu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

// TODO: Async 세부 설정
@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "messagingTaskExecutor")
    public ThreadPoolTaskExecutor messagingTaskExecutor() {
        return new ThreadPoolTaskExecutor();
    }
}
