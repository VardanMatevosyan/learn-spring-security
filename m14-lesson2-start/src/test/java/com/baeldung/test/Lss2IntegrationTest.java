package com.baeldung.test;

import com.baeldung.lss.web.controller.UserController;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import com.baeldung.lss.spring.LssApp2;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LssApp2.class, webEnvironment = WebEnvironment.NONE)
public class Lss2IntegrationTest {

    @Autowired
    UserController userApi;

    @Test
    @WithMockUser(roles = "ADMIN")
    public void whenAdminUserDeleteUser_thenSuccess() {
        StepVerifier.create(userApi.delete(1L)).verifyComplete();
    }

    @Test
    @WithMockUser(roles = "USER")
    public void whenNoAdminDeleteUser_thenAccessDenied() {
        StepVerifier.create(userApi.delete(1L)).expectError(AccessDeniedException.class).verify();
    }
}
