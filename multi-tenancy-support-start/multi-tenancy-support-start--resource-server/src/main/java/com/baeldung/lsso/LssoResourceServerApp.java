package com.baeldung.lsso;

import com.baeldung.lsso.spring.AuthServerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(AuthServerConfig.class)
@SpringBootApplication
public class LssoResourceServerApp {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(LssoResourceServerApp.class, args);
    }

}
