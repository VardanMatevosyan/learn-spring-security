package com.baeldung.lss.config;

import java.util.function.Function;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class LssSecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(
            authentication -> authentication
                .anyRequest()
                .authenticated()
        )
        .formLogin(Customizer.withDefaults());
    return http.build();
  }

  @Bean
  public PasswordEncoder delegatingPasswordEncoder() {
    DelegatingPasswordEncoder delPasswordEncoder = (DelegatingPasswordEncoder) PasswordEncoderFactories.createDelegatingPasswordEncoder();
    BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
    delPasswordEncoder.setDefaultPasswordEncoderForMatches(bcryptPasswordEncoder);
    return delPasswordEncoder;
  }

  @Bean
  public UserDetailsService userDetailsService() {
    InMemoryUserDetailsManager imMemoryManager = new InMemoryUserDetailsManager();
    imMemoryManager.createUser(getUserBuilder());
    return imMemoryManager;
  }

  @Bean
  public Function<String, String> passwordEncoder() {
    return (String password) -> delegatingPasswordEncoder().encode(password);
  }


  @Bean
  public UserDetails getUserBuilder() {
    return User
        .withUsername("vova")
        .password("vova")
        .roles("USER")
        .passwordEncoder(passwordEncoder())
        .build();
  }

}
