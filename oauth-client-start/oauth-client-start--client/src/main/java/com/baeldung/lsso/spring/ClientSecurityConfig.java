package com.baeldung.lsso.spring;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {// @formatter:off
        http.authorizeHttpRequests(
            authorize ->
                authorize.requestMatchers("/").permitAll()
                    .anyRequest().authenticated())
            .oauth2Login()
            .and()
            .logout().logoutSuccessUrl("/");
        return http.build();
    }// @formatter:on

    @Bean
    @Qualifier("tokenBasedWebClient")
    public WebClient tokenBasedWebClient(
        ClientRegistrationRepository clientRegistrationRepository,
        OAuth2AuthorizedClientRepository oAuth2AuthorizedClientRepository) {
        var filter = new ServletOAuth2AuthorizedClientExchangeFilterFunction(
            clientRegistrationRepository, oAuth2AuthorizedClientRepository);
        filter.setDefaultOAuth2AuthorizedClient(true);
        return WebClient.builder().apply(filter.oauth2Configuration()).build();
    }

}