package com.baeldung.lss.security;

import javax.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

@Component
public class LssSecurityAuthenticationDetailsSource implements AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> {

  @Override
  public WebAuthenticationDetails buildDetails(HttpServletRequest context) {
    return new LssSecurityWebAuthenticationDetails(context);
  }
}
