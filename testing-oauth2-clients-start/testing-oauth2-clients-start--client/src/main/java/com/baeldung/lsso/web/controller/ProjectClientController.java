package com.baeldung.lsso.web.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.reactive.function.client.WebClient;

import com.baeldung.lsso.web.model.ProjectModel;

@Controller
public class ProjectClientController {

    @Value("${resourceserver.api.project.url:http://localhost:8081/lsso-resource-server/api/projects/}")
    private String projectApiUrl;

    @Autowired
    private WebClient webClient;

    @GetMapping("/projects")
    public String getProjects(Model model) {
        List<ProjectModel> projects = this.webClient.get()
            .uri(projectApiUrl)
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<List<ProjectModel>>() {
            })
            .block();
        model.addAttribute("projects", projects);
        return "projects";
    }

    @GetMapping("/projects/just/test")
    public String getProjectsToTest(
        @RegisteredOAuth2AuthorizedClient("custom") OAuth2AuthorizedClient oAuth2AuthorizedClient,
        Model model) {
        List<ProjectModel> projects = this.webClient.get()
            .uri(projectApiUrl)
            .attributes(ServletOAuth2AuthorizedClientExchangeFilterFunction
                .oauth2AuthorizedClient(oAuth2AuthorizedClient))
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<List<ProjectModel>>() {
            })
            .block();
        model.addAttribute("projects", projects);
        return "projects";
    }

    @GetMapping("/addproject")
    public String addNewProject(Model model, @AuthenticationPrincipal OAuth2User oauth2User) {
        if (!hasEmailScope(oauth2User))
            return "requestpermission";
        if (!isUserEmailVerified(oauth2User))
            return "verifyemail";
        model.addAttribute("project", new ProjectModel(0L, "", LocalDate.now()));
        return "addproject";
    }

    private boolean isUserEmailVerified(OAuth2User oauth2User) {
        return (Boolean) oauth2User.getAttributes()
            .getOrDefault("email_verified", false);
    }

    private boolean hasEmailScope(OAuth2User oauth2User) {
        return oauth2User.getAuthorities()
            .contains(new SimpleGrantedAuthority("SCOPE_email"));
    }

    @PostMapping("/projects")
    public String saveProject(ProjectModel project, Model model) {
        try {
            this.webClient.post()
                .uri(projectApiUrl)
                .bodyValue(project)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
            return "redirect:/projects";
        } catch (final HttpServerErrorException e) {
            model.addAttribute("msg", e.getResponseBodyAsString());
            return "addproject";
        }
    }
}
