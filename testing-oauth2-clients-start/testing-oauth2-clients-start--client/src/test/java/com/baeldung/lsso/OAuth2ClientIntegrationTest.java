package com.baeldung.lsso;

import static org.hamcrest.Matchers.containsString;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

import java.util.Map;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class OAuth2ClientIntegrationTest {

    @Autowired
    private MockMvc mvc;

    private static MockWebServer resourceServer;

    @BeforeAll
    public static void startServers() throws Exception {
        resourceServer = new MockWebServer();
        resourceServer.start(8081);
    }

    @AfterAll
    public static void tearDown() throws Exception {
        resourceServer.shutdown();
    }

    @Test
    @WithMockUser
    void givenMockedUser_whenRequestResources_thenOK() throws Exception {
        String mockedResources = "[{\"id\":1,\"name\":\"Project 1\",\"dateCreated\":\"2025-06-01\"}]";

        resourceServer.enqueue(new MockResponse().setBody(mockedResources)
            .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));

        mvc.perform(get("/projects"))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Project 1")));
    }

    @Test
    void givenMockedUser_whenRequestResourcesWithOauth2LoginDirective_thenOK() throws Exception {
        String mockedResources = "[{\"id\":1,\"name\":\"Project 1\",\"dateCreated\":\"2025-06-01\"}]";

        resourceServer.enqueue(new MockResponse().setBody(mockedResources)
            .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));

        mvc.perform(get("/projects/just/test").with(oauth2Login()))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Project 1")));
    }

    @Test
    void givenEndpointRequireEmailVerifiedAttrAndEmailScope_whenNoEmailScope_thenRouteToReqPermissionPage() throws Exception {
        this.mvc.perform(get("/addproject").with(oauth2Login().attributes(attrs -> attrs.put("email_verified", true))))
            .andExpect(status().isOk())
            .andExpect(xpath("//button[@type='button']").string("Request Permission"));
    }

    @Test
    void givenEndpointRequireEmailVerifiedAttrAndEmailScope_whenEmailNotVerified_thenRouteToVerifyEmailPage() throws Exception {
        this.mvc.perform(get("/addproject").with(oauth2Login().authorities(new SimpleGrantedAuthority("SCOPE_email"))))
            .andExpect(status().isOk())
            .andExpect(xpath("//button[@type='button']").string("Go Back"));
    }

    @Test
    void givenEndpointRequireEmailVerifiedAttrAndEmailScope_whenAllAttrsSet_thenRouteToAddProjectPage()
        throws Exception {
        //using customized oauth2User object
        OAuth2User oauth2User = new DefaultOAuth2User(
            AuthorityUtils.createAuthorityList("SCOPE_email"),
            Map.of("email_verified", true, "username", "principal_name"),
            "username");

        mvc.perform(get("/addproject").with(oauth2Login().oauth2User(oauth2User)))
            .andExpect(status().isOk())
            .andExpect(xpath("//button[@type='submit']").string("Create Project"));
    }

}
