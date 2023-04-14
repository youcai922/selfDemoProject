package com.yc.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author yucan
 * @date 2022/11/11 16:48
 */
@EnableScheduling
@SpringBootApplication(scanBasePackages = "com.yc.test.**")
public class QqApplication {
    public static void main(String[] args) {
        SpringApplication.run(QqApplication.class, args);
    }
}