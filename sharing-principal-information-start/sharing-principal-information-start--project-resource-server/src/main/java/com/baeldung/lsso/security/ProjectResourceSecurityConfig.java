package com.baeldung.lsso.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedGrantedAuthoritiesUserDetailsService;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class ProjectResourceSecurityConfig {

  @Autowired
  private AuthenticationConfiguration authenticationConfiguration;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
        .authorizeHttpRequests()
        .requestMatchers(HttpMethod.GET, "/api/projects/**").hasAuthority("SCOPE_read")
        .requestMatchers(HttpMethod.POST, "/api/projects").hasAuthority("SCOPE_write")
        .anyRequest().authenticated()

        .and()

        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        .and()

        .csrf().disable()
        .addFilterAt(preAuthFilter(), AbstractPreAuthenticatedProcessingFilter.class)
        .build();
  }

  @Bean
  public AbstractPreAuthenticatedProcessingFilter preAuthFilter() throws Exception {
    RequestHeaderAuthenticationFilter preAuthFilter = new RequestHeaderAuthenticationFilter();
    preAuthFilter.setPrincipalRequestHeader("BAEL-username");
    preAuthFilter.setAuthenticationManager(authenticationConfiguration.getAuthenticationManager());
    preAuthFilter.setAuthenticationDetailsSource(new CustomAuthenticationDetailsSource());
    return preAuthFilter;
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    var preAuthProvider = new PreAuthenticatedAuthenticationProvider();
    preAuthProvider.setPreAuthenticatedUserDetailsService(new PreAuthenticatedGrantedAuthoritiesUserDetailsService());
    return preAuthProvider;
  }

}
