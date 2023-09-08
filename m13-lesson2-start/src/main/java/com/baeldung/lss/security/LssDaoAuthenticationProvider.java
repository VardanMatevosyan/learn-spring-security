package com.baeldung.lss.security;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class LssDaoAuthenticationProvider extends DaoAuthenticationProvider {

  @Autowired
  private UserDetailsService userDetailsService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public LssDaoAuthenticationProvider() {
    super();
  }

  @PostConstruct
  private void afterInit() {
    this.setUserDetailsService(userDetailsService);
    this.setPasswordEncoder(passwordEncoder);
  }

  //    authentication is object is from the request after authentication
  //    userDetails is authenticated principal user retrieved by userDetail service from the db
  //    We're doing here addition check if user tenant is the same that from the request
  @Override
  protected void additionalAuthenticationChecks(
      UserDetails userDetails,
      UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
    LssPrincipalUser principalUser = (LssPrincipalUser) userDetails;
    if (!principalUser.getTenant().equals(authentication.getDetails())) {
      throw new BadCredentialsException("Invalid credentials");
    }
    super.additionalAuthenticationChecks(userDetails, authentication);
  }

}
