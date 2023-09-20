package com.baeldung.lsso.spring;

import static org.springframework.security.authorization.AuthorityAuthorizationManager.hasAuthority;
import static org.springframework.security.authorization.AuthorizationManagers.allOf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ResourceSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {// @formatter:off
        http.authorizeHttpRequests(authorize -> authorize
	              .requestMatchers(HttpMethod.GET, "/api/projects/**")
	                .access(allOf(hasAuthority("SCOPE_read"), hasAuthority("EMAIL_USERNAME")))
	              .requestMatchers(HttpMethod.POST, "/api/projects")
	                .hasAuthority("SCOPE_write")
	              .anyRequest()
	                .authenticated())
              .oauth2ResourceServer()
                .jwt();
        return http.build();
    }//@formatter:on

	@Bean
	public JwtAuthenticationConverter customJwtAuthenticationConverter() {
		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new CustomAuthoritiesExtractor());
		return jwtAuthenticationConverter;
	}

}