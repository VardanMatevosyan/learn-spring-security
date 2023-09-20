package com.baeldung.lsso.spring;

import java.util.Collection;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

public class CustomAuthoritiesExtractor implements Converter<Jwt, Collection<GrantedAuthority>> {

  @Override
  public Collection<GrantedAuthority> convert(Jwt source) {
    JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    jwtGrantedAuthoritiesConverter.setAuthorityPrefix("SCOPE_"); // can be change if needed
    jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("scope"); // usually this is common name for scopes
    Collection<GrantedAuthority> authorities = jwtGrantedAuthoritiesConverter.convert(source);
    String username = source.getClaimAsString("preferred_username");
    if (username.contains("@")) {
      authorities.add(new SimpleGrantedAuthority("EMAIL_USERNAME"));
    }
    return authorities;
  }
}
