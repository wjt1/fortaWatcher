package com.web3.fortawather;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FortaWatherApplication {

    public static void main(String[] args) {
        SpringApplication.run(FortaWatherApplication.class, args);
    }

}
