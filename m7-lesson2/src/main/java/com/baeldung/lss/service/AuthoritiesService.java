package com.baeldung.lss.service;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthoritiesService {

  @Secured({"ROLE_RUN_AS_REPORTER"})
  public String getAuthorities() {
    return SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
  }
}
