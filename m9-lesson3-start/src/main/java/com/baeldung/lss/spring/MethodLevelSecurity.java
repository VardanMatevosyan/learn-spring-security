package com.baeldung.lss.spring;

import java.util.HashMap;
import java.util.List;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.method.MapBasedMethodSecurityMetadataSource;
import org.springframework.security.access.method.MethodSecurityMetadataSource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MethodLevelSecurity extends GlobalMethodSecurityConfiguration {

  @Override
  public MethodSecurityMetadataSource customMethodSecurityMetadataSource() {
    HashMap<String, List<ConfigAttribute>> metadataSource = new HashMap<>();
    metadataSource.put(
        "com.baeldung.lss.web.controller.UserController.createForm*",
        SecurityConfig.createList("ROLE_ADMIN"));
    return new MapBasedMethodSecurityMetadataSource(metadataSource);
  }

}
