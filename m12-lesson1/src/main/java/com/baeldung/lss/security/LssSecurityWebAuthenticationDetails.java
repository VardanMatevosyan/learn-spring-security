package com.baeldung.lss.security;

import javax.servlet.http.HttpServletRequest;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

public class LssSecurityWebAuthenticationDetails extends WebAuthenticationDetails {

  private final String verificationCode;

  public LssSecurityWebAuthenticationDetails(HttpServletRequest request) {
    super(request);
    verificationCode = request.getParameter("code");
  }

  public String getVerificationCode() {
    return verificationCode;
  }
}
