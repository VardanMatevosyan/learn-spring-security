package com.baeldung.lsso.spring;

import static java.util.Objects.nonNull;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

public class CustomUsernameClaimValidator implements OAuth2TokenValidator<Jwt> {

  private static final String ALLOWED_DOMAIN = "@test.com";
  private static final String CUSTOM_CLAIM_NAME = "preferred_username";
  private static final String USERNAME_CLAIM_ERR_MSG = "Only @test.com users are allowed access";
  private static final String ACCESS_DENIED  = "access_denied";

  @Override
  public OAuth2TokenValidatorResult validate(Jwt token) {
    String username = token.getClaimAsString(CUSTOM_CLAIM_NAME);
    if (nonNull(username) && username.toLowerCase().contains(ALLOWED_DOMAIN)) {
      return OAuth2TokenValidatorResult.success();
    }
    return OAuth2TokenValidatorResult
        .failure(new OAuth2Error(ACCESS_DENIED, USERNAME_CLAIM_ERR_MSG,null));
  }
}
