package com.baeldung.lss.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthProvider implements AuthenticationProvider {

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    if (doseNotSupportAuthentication(authentication)) {
      return null;
    }
    if (doAuthenticationAgainstThirdPartySystem()) {
      return getAuthenticationToken(authentication);
    } else {
      throw new BadCredentialsException("Authentication with third party system failed");
    }
  }

  private static Authentication getAuthenticationToken(Authentication authentication) {
    return new UsernamePasswordAuthenticationToken(
        authentication.getName(),
        authentication.getCredentials(),
        authentication.getAuthorities()); // here if you need specific authorities for provider we can change this
  }

  // just an example in what case we can return null from the authentication object.
  private boolean doseNotSupportAuthentication(Authentication authentication) {
    return false;
  }

  // just an example imagine that we need to support third party authentication
  // for this custom provider and if yes then return full Authentication object
  private boolean doAuthenticationAgainstThirdPartySystem() {
    return true;
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
  }
}
