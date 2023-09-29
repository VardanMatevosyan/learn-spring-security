package com.baeldung.lsso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

@EnableAuthorizationServer
@SpringBootApplication
public class LssoAuthorizationServerApp {

    public static void main(String[] args) {
        SpringApplication.run(LssoAuthorizationServerApp.class, args);
    }

}
