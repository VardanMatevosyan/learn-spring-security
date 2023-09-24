package com.baeldung.lsso.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientSecurityConfig {


  private final ClientRegistrationRepository clientRegistrationRepository;

  @Autowired
  public ClientSecurityConfig(ClientRegistrationRepository clientRegistrationRepository) {
    this.clientRegistrationRepository = clientRegistrationRepository;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {// @formatter:off
        http.authorizeHttpRequests(authorize -> authorize
	            .requestMatchers("/").permitAll()
	            .anyRequest().authenticated())
            .oauth2Login()
            .and()
            .logout(logout -> logout
                .logoutSuccessHandler(oidcLogoutSuccessHandler())
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
            );
        return http.build();
    }// @formatter:on


  // todo investigate this later. How to logout user from GitHub provider.
  // Doesn't work because:
  // 1. by default spring security creates DefaultOAuth2User
  // using oauth2 classes, but the logout handler works only for DefaultOidcUser's that implement OidcUser and
  // DefaultOauth2User is not OidcUser.
  // 2. GitHub doesn't provide the well-known endpoint and maybe doesn't provide end_session_endpoint
  // and so can't support RP-Initiated Logout flow
  // 3. GitHub not supported Open id connect.
  // https://docs.github.com/en/apps/oauth-apps/building-oauth-apps/authorizing-oauth-apps
  // 4. Also they not support PKCE https://docs.github.com/en/apps/oauth-apps/building-oauth-apps/authorizing-oauth-apps
  private LogoutSuccessHandler oidcLogoutSuccessHandler() {
    OidcClientInitiatedLogoutSuccessHandler oidcLogoutSuccessHandler =
        new OidcClientInitiatedLogoutSuccessHandler(this.clientRegistrationRepository);
    oidcLogoutSuccessHandler.setPostLogoutRedirectUri("{baseUrl}");
    return oidcLogoutSuccessHandler;
  }

  @Bean
  WebClient webClient(ClientRegistrationRepository clientRegistrationRepository,
      OAuth2AuthorizedClientRepository authorizedClientRepository) {
    ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2 = new ServletOAuth2AuthorizedClientExchangeFilterFunction(
        clientRegistrationRepository, authorizedClientRepository);
    oauth2.setDefaultOAuth2AuthorizedClient(true);
    return WebClient.builder()
        .apply(oauth2.oauth2Configuration())
        .build();
  }
}