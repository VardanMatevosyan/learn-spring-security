package com.baeldung.lsso.config;

import java.security.KeyPair;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .authorizeRequests()
        .antMatchers("/endpoint/jwks.json").permitAll()
        .anyRequest().authenticated()
        .and()
        .formLogin();
  }

  @Bean
  public KeyPair keyPair() {
    var keyStoreFactory = new KeyStoreKeyFactory(new ClassPathResource("mytest.jks"),
                                                "mypass".toCharArray());
    return keyStoreFactory.getKeyPair("mytest");
  }

  @Bean
  public PasswordEncoder encoder() {
    return new BCryptPasswordEncoder();
  }

}
