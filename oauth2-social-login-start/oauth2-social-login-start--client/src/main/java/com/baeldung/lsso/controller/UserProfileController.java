package com.baeldung.lsso.controller;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserProfileController {

  private WebClient webClient;

  @Autowired
  public UserProfileController(WebClient webClient) {
    this.webClient = webClient;
  }

  @GetMapping("/user")
  public String welcomePage(Model model, @AuthenticationPrincipal OAuth2User principal) {
    model.addAttribute("name", principal.getAttribute("name"));
    model.addAttribute("id", principal.getAttribute("login"));
    model.addAttribute("img", principal.getAttribute("avatar_url"));
    String reposUrl = principal.getAttribute("repos_url");
    model.addAttribute("repos", getRepositories(reposUrl));
    return "user";
  }

  private List<Map<Object, Object>> getRepositories(String reposUrl) {
    return this.webClient
        .get()
        .uri(reposUrl)
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<List<Map<Object, Object>>>() {})
        .block();
  }

}
