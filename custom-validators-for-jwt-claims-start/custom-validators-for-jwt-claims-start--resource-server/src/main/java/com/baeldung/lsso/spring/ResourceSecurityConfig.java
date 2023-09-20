package com.baeldung.lsso.spring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ResourceSecurityConfig {

	@Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
	private String jwkSetUri;

	@Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
	private String issuer;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {// @formatter:off
        http.authorizeHttpRequests(authorize -> authorize
	              .requestMatchers(HttpMethod.GET, "/api/projects/**")
	                .hasAuthority("SCOPE_read")
	              .requestMatchers(HttpMethod.POST, "/api/projects")
	                .hasAuthority("SCOPE_write")
	              .anyRequest()
	                .authenticated())
              .oauth2ResourceServer()
                .jwt();
        return http.build();
    }//@formatter:on

	@Bean
	public JwtDecoder jwtDecoder() {
		NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
		OAuth2TokenValidator<Jwt> defaultValidators = JwtValidators.createDefaultWithIssuer(issuer);
		DelegatingOAuth2TokenValidator<Jwt> validators =
				new DelegatingOAuth2TokenValidator<>(defaultValidators, new CustomUsernameClaimValidator());
		jwtDecoder.setJwtValidator(validators);
		return jwtDecoder;
	}

}