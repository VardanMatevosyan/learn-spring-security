package com.baeldung.lsso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class LssoClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(LssoClientApplication.class, args);
    }
}
