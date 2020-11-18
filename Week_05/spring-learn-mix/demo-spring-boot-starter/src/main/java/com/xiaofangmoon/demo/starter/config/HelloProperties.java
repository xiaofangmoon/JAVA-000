package com.xiaofangmoon.demo.starter.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("my.hello")
@Data
public class HelloProperties {
    String name;
    String age;
    String hometown;
}
