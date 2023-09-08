package com.baeldung.lss.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class GlobalAuthenticationConfig extends GlobalAuthenticationConfigurerAdapter {

  PasswordEncoder passwordEncoder;

  @Autowired
  public GlobalAuthenticationConfig(PasswordEncoder passwordEncoder) {
    super();
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public void init(AuthenticationManagerBuilder auth) throws Exception {
    auth
        .ldapAuthentication()
        .userSearchBase("ou=people")
        .userSearchFilter("uid={0}")
        .groupSearchBase("ou=roles")
        .groupSearchFilter("member={0}")
        .contextSource()
        .root("dc=springframework,dc=org")
        .ldif("classpath:users.ldif")
        .and()
        .passwordCompare()
        .passwordEncoder(passwordEncoder)
        .passwordAttribute("userPassword");
  }
}
