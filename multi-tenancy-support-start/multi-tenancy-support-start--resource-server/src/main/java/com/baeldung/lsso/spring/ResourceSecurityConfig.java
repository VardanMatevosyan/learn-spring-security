package com.baeldung.lsso.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerAuthenticationManagerResolver;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ResourceSecurityConfig {

	private final AuthServerConfig authServerConfig;

	@Autowired
	public ResourceSecurityConfig(AuthServerConfig authServerConfig) {
		this.authServerConfig = authServerConfig;
	}

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {// @formatter:off
        http.authorizeHttpRequests(authorize -> authorize
	              .requestMatchers(HttpMethod.GET, "/api/projects/**")
	                .hasAuthority("SCOPE_read")
	              .requestMatchers(HttpMethod.POST, "/api/projects")
	                .hasAuthority("SCOPE_write")
	              .anyRequest()
	                .authenticated())
              .oauth2ResourceServer(rs ->
									rs.authenticationManagerResolver(jwtIssuerAuthenticationManagerResolver()));
        return http.build();
    }//@formatter:on

	@Bean
	public JwtIssuerAuthenticationManagerResolver jwtIssuerAuthenticationManagerResolver() {
    return new JwtIssuerAuthenticationManagerResolver(issuer -> {
			NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder
					.withJwkSetUri(authServerConfig.getIssuersJwkSetUri().get(issuer))
					.jwsAlgorithm(SignatureAlgorithm.RS256)
					.build();
			var authProvider = new JwtAuthenticationProvider(jwtDecoder);
			return new ProviderManager(authProvider);
		});
	}

}