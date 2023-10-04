package com.baeldung.lsso.spring;

import org.springframework.cloud.gateway.config.GlobalCorsProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.DelegatingServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository;
import org.springframework.security.web.server.csrf.ServerCsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsConfigurationSource;

@Configuration
public class LssoGatewaySecurity {

  @Bean
  public CorsConfigurationSource corsConfig(GlobalCorsProperties corsProperties) {
    UrlBasedCorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();
    corsProperties.getCorsConfigurations()
        .forEach(corsConfigurationSource::registerCorsConfiguration);
    return corsConfigurationSource;
  }

  @Bean
  public SecurityWebFilterChain filterChain(ServerHttpSecurity http) {
    var csrfRepository = CookieServerCsrfTokenRepository.withHttpOnlyFalse();
    var requestHandler = new ServerCsrfTokenRequestAttributeHandler();
    return http
        .authorizeExchange()
        .anyExchange().authenticated()
        .and()
        .oauth2Login(customizer -> customizer.authenticationSuccessHandler(
            new DelegatingServerAuthenticationSuccessHandler(
                getCookieCsrfHandler(csrfRepository),
                getRedirectSuccessHandler())))
        .csrf(csrfSpec -> {
          csrfSpec.csrfTokenRepository(csrfRepository);
          csrfSpec.csrfTokenRequestHandler(requestHandler);
        })
        .build();
  }

  private ServerAuthenticationSuccessHandler getCookieCsrfHandler(CookieServerCsrfTokenRepository csrfRepository) {
    return (webFilterExchange, authentication) -> {
      return csrfRepository
          .generateToken(webFilterExchange.getExchange())
          .delayUntil(token -> csrfRepository.saveToken(webFilterExchange.getExchange(), token))
          .then();
    };
  }

  private static RedirectServerAuthenticationSuccessHandler getRedirectSuccessHandler() {
    return new RedirectServerAuthenticationSuccessHandler("http://localhost:8082/lsso-client/");
  }

}
