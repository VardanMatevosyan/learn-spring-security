package com.baeldung.lss.spring;

import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class LssSecurityConfig {

//  // INFO First implementation ===>
  private final PasswordEncoder passwordEncoder;

  public LssSecurityConfig(PasswordEncoder passwordEncoder) {
    super();
    this.passwordEncoder = passwordEncoder;
  }
//  // INFO First implementation <===

//   INFO Second implementation ===>
//    @Bean // can be simple new BCryptPasswordEncoder()
//    public PasswordEncoder delegatingPasswordEncoder() {
//        DelegatingPasswordEncoder delPasswordEncoder = (DelegatingPasswordEncoder) PasswordEncoderFactories.createDelegatingPasswordEncoder();
//        BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
//        delPasswordEncoder.setDefaultPasswordEncoderForMatches(bcryptPasswordEncoder);
//        return bcryptPasswordEncoder;
//    }
//
//    @Bean
//    public UserDetailsService userDetailsService() {
//        InMemoryUserDetailsManager imMemoryManager = new InMemoryUserDetailsManager();
//        imMemoryManager.createUser(getUserBuilder());
//        return imMemoryManager;
//    }
//
//    @Bean
//    public Function<String, String> passwordEncoder() {
//        return (String password) -> delegatingPasswordEncoder().encode(password);
//    }
//
//
//    @Bean
//    public UserDetails getUserBuilder() {
//        return User
//            .withUsername("vova")
//            .password("vova")
//            .passwordEncoder(passwordEncoder())
//            .authorities("MODERATOR", "ADMIN")
//            .build();
//    }
//   INFO Second implementation <===

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests((authentication) -> authentication
            .requestMatchers("/delete/**").hasAnyAuthority("ADMIN", "MODERATOR")
            .anyRequest().authenticated()
        )
        .formLogin(Customizer.withDefaults())
        .httpBasic(Customizer.withDefaults());
    return http.build();
  }


//  // INFO First implementation ===>
  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception { // @formatter:off
        auth.
            inMemoryAuthentication().passwordEncoder(passwordEncoder).
            withUser("user").password(passwordEncoder.encode("pass")).
            authorities("ADMIN");
    } // @formatter:on
//  // INFO First implementation <===

}
