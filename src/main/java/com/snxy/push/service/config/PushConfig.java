package com.snxy.push.service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhuchangbin
 * @date 2017/10/16
 */
@Configuration
public class PushConfig {

    @Bean
    @ConfigurationProperties(prefix = "push")
    public PushConfigBean pushConfigBean() {
        return new PushConfigBean();
    }
}
