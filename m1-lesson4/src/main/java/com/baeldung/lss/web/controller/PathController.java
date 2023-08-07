package com.baeldung.lss.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PathController {

  @GetMapping("/login")
  public String doLogin() {
    return "loginPage";
  }

}
