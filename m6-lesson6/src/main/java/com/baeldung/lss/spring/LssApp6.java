package com.baeldung.lss.spring;

import java.security.SecureRandom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@ComponentScan("com.baeldung.lss")
@EnableJpaRepositories("com.baeldung.lss")
@EntityScan("com.baeldung.lss.model")
public class LssApp6 {

    @Bean
    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder(10, new SecureRandom());
//        Can be without  new SecureRandom() because BCryptPasswordEncoder creates SecureRandom internally
        // 500 ms average is ok to log in
//        return new BCryptPasswordEncoder(19); takes 40 seconds to login not recommended
//        return new BCryptPasswordEncoder(12); takes 800 ms to login not bad but can be to long for the user
        return new BCryptPasswordEncoder(11); // takes about 140 ms but sometimes about 400 ms to login which is preferred one
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(new Class[] { LssApp6.class, LssSecurityConfig.class, LssWebMvcConfiguration.class }, args);
    }

}
