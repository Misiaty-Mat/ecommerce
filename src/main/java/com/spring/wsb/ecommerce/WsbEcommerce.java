package com.spring.wsb.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.spring.wsb.ecommerce.entity")
public class WsbEcommerce {

    public static void main(String[] args) {
        SpringApplication.run(WsbEcommerce.class, args);
    }

}
