package com.baeldung.lsso;


import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.baeldung.lsso.spring.CustomAuthoritiesConverter;
import com.baeldung.lsso.web.dto.ProjectDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.time.Instant;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class ResourceServerTestingSupportIntegrationTest {
    
    @Autowired
    private MockMvc mvc;


    @Test
    public void test_receivedProjects_whenGivenJwtToken_thenIsOk() throws Exception {
        mvc.perform(get("/api/projects").with(jwt()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()", Matchers.greaterThan(0)))
            .andExpect(jsonPath("$..name", Matchers.hasItems("Project 1", "Project 2")));
    }

    @Test
    public void test_createProject_whenJwtScopeIsNotCorrect_thenIsForbidden() throws Exception {
        mvc.perform(
            post("/api/projects")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(getTestProjectBody()))
            .andExpect(status().isForbidden());
    }

    @Test
    public void test_createProject_whenJwtWithCorrectScope_thenIsOK() throws Exception {
        ProjectDto projectDto = buildTestProject();
        mvc.perform(
                post("/api/projects")
                    .with(jwt().jwt(jwtBuilder -> jwtBuilder.claim("scope", "write")))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(getTestProjectBody(projectDto)))
            .andExpect(status().isCreated());

        checkCreatedProjects(projectDto);
    }

    @Test
    public void test_createProjectWithCustomJwtConverter_whenJwtWithAdminSubject_thenIsOK() throws Exception {
        ProjectDto projectDto = buildTestProject();
        mvc.perform(
                post("/api/projects")
                    .with(
                        jwt()
                            .jwt(jwtBuilder -> jwtBuilder.subject("admin"))
                            .authorities(new CustomAuthoritiesConverter())
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(getTestProjectBody(projectDto)))
            .andExpect(status().isCreated());

        checkCreatedProjects(projectDto);
    }

    @Test
    public void test_createProjectDirectlyWithAuthorities_whenJwtWithAdminSubject_thenIsOK() throws Exception {
        ProjectDto projectDto = buildTestProject();
        mvc.perform(
                post("/api/projects")
                    .with(
                        jwt().authorities(new SimpleGrantedAuthority("SCOPE_write"))
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(getTestProjectBody(projectDto)))
            .andExpect(status().isCreated());

        checkCreatedProjects(projectDto);
    }

    @Test
    public void test_getProjects_whenTokenIsExpired_thenBypassSecurityToTestTheLogic() throws Exception {
        mvc.perform(get("/api/projects")
                .with(jwt().jwt(jwtBuilder ->
                    jwtBuilder.expiresAt(Instant.now().minus(Duration.ofDays(5L))))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()", Matchers.greaterThan(0)))
            .andExpect(jsonPath("$..name", Matchers.hasItems("Project 1", "Project 2")));
    }

    private void checkCreatedProjects(ProjectDto projectDto) throws Exception {
        mvc.perform(get("/api/projects").with(jwt()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()", Matchers.greaterThan(3)))
            .andExpect(jsonPath("$..name", Matchers.hasItems(projectDto.name())));
    }

    private static String getTestProjectBody() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(buildTestProject());
    }

    private static String getTestProjectBody(ProjectDto projectDto) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(projectDto);
    }

    private static ProjectDto buildTestProject() {
        return new ProjectDto(4L, "New Project 4", null);
    }

}
