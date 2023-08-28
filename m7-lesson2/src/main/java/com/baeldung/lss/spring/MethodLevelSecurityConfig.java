package com.baeldung.lss.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.intercept.RunAsManager;
import org.springframework.security.access.intercept.RunAsManagerImpl;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class MethodLevelSecurityConfig extends GlobalMethodSecurityConfiguration {

  @Override
  public RunAsManager runAsManager() {
    RunAsManagerImpl runAsManager = new RunAsManagerImpl();
    runAsManager.setKey("runAsManager");
    return runAsManager;
  }

}
