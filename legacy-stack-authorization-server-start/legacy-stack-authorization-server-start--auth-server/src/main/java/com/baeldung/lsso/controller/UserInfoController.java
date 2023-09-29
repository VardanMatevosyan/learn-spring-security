package com.baeldung.lsso.controller;

import java.util.Collections;
import java.util.Map;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserInfoController {

  @GetMapping("/users/userinfo")
  public Map<String, Object> userInfo(OAuth2Authentication oAuth2Authentication) {
    return Collections.singletonMap("preferred_username", oAuth2Authentication.getName());
  }

}
