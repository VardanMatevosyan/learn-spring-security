package com.baeldung.lsso.security;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.core.authority.GrantedAuthoritiesContainer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails;

public class CustomAuthenticationDetailsSource implements AuthenticationDetailsSource<HttpServletRequest, GrantedAuthoritiesContainer>{

  @Override
  public GrantedAuthoritiesContainer buildDetails(HttpServletRequest context) {
    Enumeration<String> headerValues = context.getHeaders("BAEL-authorities");
    List<SimpleGrantedAuthority> authorities = Collections.list(headerValues)
        .stream()
        .map(SimpleGrantedAuthority::new)
        .toList();
    return new PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails(context, authorities);
  }
}
