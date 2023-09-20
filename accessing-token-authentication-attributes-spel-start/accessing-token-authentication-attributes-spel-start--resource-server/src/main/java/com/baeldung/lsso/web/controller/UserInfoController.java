package com.baeldung.lsso.web.controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserInfoController {
    
    @GetMapping("/user/info/spel1")
    public Map<String, Object> getUserInfo(@AuthenticationPrincipal(expression = "claims") Map<String, Object> claims) {
        return Collections.singletonMap("token", claims.get("preferred_username"));
    }

    @PreAuthorize(value = "principal?.claims['scope']?.contains('read')")
    @GetMapping("/user/info/spel2")
    public Map<String, Object> getReadScopeResponse() {
        return Collections.singletonMap("response", "users with the read scope can see this");
    }
}
