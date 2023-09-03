package com.baeldung.lss.service;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.provisioning.UserDetailsManagerResourceFactoryBean;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
public class UsersFromPropertiesFile {

  // To create test users on startup in memory from property file approach
  @Bean("fromPropFile")
  public FactoryBean<? extends UserDetailsService> userDetailsFactoryBean() {
    return UserDetailsManagerResourceFactoryBean.fromResourceLocation("classpath:users.properties");
  }

}
