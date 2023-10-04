package com.baeldung.lsso.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ProjectSecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests()
        .requestMatchers(HttpMethod.GET, "/api/projects/**").hasAuthority("SCOPE_read")
        .requestMatchers(HttpMethod.POST, "/api/projects").hasAuthority("SCOPE_write")
        .anyRequest().authenticated()
        .and()
        .oauth2ResourceServer().jwt();
    return http.build();
  }

}
