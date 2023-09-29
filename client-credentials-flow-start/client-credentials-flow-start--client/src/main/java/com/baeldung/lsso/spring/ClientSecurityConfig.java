package com.baeldung.lsso.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientSecurityConfig {

  @Bean
  WebClient webClient(OAuth2AuthorizedClientManager manager) {
    var oauth2ExchangeFunction = new ServletOAuth2AuthorizedClientExchangeFilterFunction(manager);
    oauth2ExchangeFunction.setDefaultClientRegistrationId("customClientCredentials");
    return WebClient.builder().apply(oauth2ExchangeFunction.oauth2Configuration()).build();
  }

  @Bean
  public OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager(
      ClientRegistrationRepository clientRegistrationRepository,
      OAuth2AuthorizedClientService oAuth2AuthorizedClientService) {

    var provider = OAuth2AuthorizedClientProviderBuilder.builder().clientCredentials().build();
    var manager = new AuthorizedClientServiceOAuth2AuthorizedClientManager(
        clientRegistrationRepository, oAuth2AuthorizedClientService);
    manager.setAuthorizedClientProvider(provider);
    return manager;
  }

}