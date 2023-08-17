package com.baeldung.lss.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthenticatedAuthorizationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class LssSecurityConfig {

    private PasswordEncoder passwordEncoder;

    public LssSecurityConfig(PasswordEncoder passwordEncoder) {
        super();
        this.passwordEncoder = passwordEncoder;
    }

    //

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception { // @formatter:off 
        auth.
            inMemoryAuthentication().passwordEncoder(passwordEncoder)
            .withUser("user").password(passwordEncoder.encode("pass")).roles("USER").and()
            .withUser("admin").password(passwordEncoder.encode("pass")).roles("ADMIN")
            ;
    } // @formatter:on

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {// @formatter:off
        http
        .authorizeRequests()
// access only for the users with role ROLE_ADMIN
            .antMatchers("/secured/**").access("hasRole('ADMIN')")
// combination of spring security expression with ROLE_ADMIN and username user
//            .antMatchers("/secured/**").access ("hasRole('ROLE_ADMIN') and principal.username == 'user'")
// combination of spring security expression with ROLE_ADMIN or username user
// can be ROLE_ADMIN as well as user with username user
//            .antMatchers("/secured/**").access("hasRole('ROLE_ADMIN') or principal.username == 'user'")
// access for all not authenticated users
//            .antMatchers("/secured/**").anonymous()
//            .antMatchers("/secured/**").access("isAnonymous()")
// access only for the users with role ROLE_USER
//             .antMatchers("/secured/**").access("hasAuthority('ROLE_USER')")
// access only for ip v4 address
//             .antMatchers("/secured/**").hasIpAddress("192.168.1.0/24")
// access only for ip v6 localhost address
//             .antMatchers("/secured/**").hasIpAddress("::1")
// access allowed only for GET request for /secured endpoint
//            .antMatchers("/secured/**").access("request.method == 'GET'")
// access allowed for any method but not for POST request for /secured endpoint
//            .antMatchers("/secured/**").access("request.method != 'POST'")
// access for all the ips but not for localhost
//            .antMatchers("/secured/**").not().access("hasIpAddress('::1')")
            .anyRequest().authenticated()
        
        .and()
        .formLogin().
            loginPage("/login").permitAll().
            loginProcessingUrl("/doLogin")

        .and()
        .logout().permitAll().logoutUrl("/logout")
        
        .and()
        .csrf().disable();
        return http.build();
    }

}

