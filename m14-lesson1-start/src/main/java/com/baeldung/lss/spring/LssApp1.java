package com.baeldung.lss.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;

import com.baeldung.lss.persistence.InMemoryUserRepository;
import com.baeldung.lss.persistence.UserRepository;
import com.baeldung.lss.web.model.User;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;

@SpringBootApplication(scanBasePackages = { "com.baeldung.lss.web", "com.baeldung.lss.spring"})
public class LssApp1 {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public UserRepository userRepository() {
        return new InMemoryUserRepository();
    }

    /**
     * Not used due to - java.lang.IllegalStateException: PathVariableMethodArgumentResolver doesn't support reactive type wrapper     
     */
    @Bean
    public Converter<String, Mono<User>> monoMessageConverter() {
        return new Converter<String, Mono<User>>() {
            @Override
            public Mono<User> convert(String id) {
                return userRepository().findUser(Long.valueOf(id));
            }
        };
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(LssApp1.class, args);
    }

}
