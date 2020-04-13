package com.zps.bootwithredis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@SpringBootApplication
public class BootwithredisApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootwithredisApplication.class, args);
    }

}
