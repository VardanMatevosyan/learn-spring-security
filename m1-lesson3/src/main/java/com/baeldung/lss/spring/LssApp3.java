package com.baeldung.lss.spring;

import com.baeldung.lss.persistence.InMemoryUserRepository;
import com.baeldung.lss.persistence.UserRepository;
import com.baeldung.lss.web.model.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@ComponentScan("com.baeldung.lss.web")
public class LssApp3 {

    @Bean
    public UserRepository userRepository() {
        return new InMemoryUserRepository();
    }


    // INFO First implementation ===>
    // Somehow with First implementation, when using new version of spring security with filter chain bean
    // without extending web security adapter when adding this bean declaration in the
    // configuration class and running it shows circular dependency error
    // ?????????????????????????????
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    // INFO First implementation <===

    @Bean
    public Converter<String, User> messageConverter() {
        return new Converter<String, User>() {
            @Override
            public User convert(String id) {
                return userRepository().findUser(Long.valueOf(id));
            }
        };
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(new Class[] { LssApp3.class, LssSecurityConfig.class }, args);
    }

}
