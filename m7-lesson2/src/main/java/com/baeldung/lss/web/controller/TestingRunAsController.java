package com.baeldung.lss.web.controller;

import com.baeldung.lss.service.AuthoritiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestingRunAsController {

  @Autowired
  AuthoritiesService authoritiesService;


  @RequestMapping("/runas")
  @Secured({"ROLE_USER", "RUN_AS_REPORTER"})
  @ResponseBody
  public String getAuthorities() {
    return authoritiesService.getAuthorities();
  }

}
