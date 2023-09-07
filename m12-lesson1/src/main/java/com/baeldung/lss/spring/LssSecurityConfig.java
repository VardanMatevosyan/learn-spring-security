package com.baeldung.lss.spring;

import com.baeldung.lss.security.LssSecurityAuthenticationDetailsSource;
import com.baeldung.lss.security.LssUsernamePasswordAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@ComponentScan({ "com.baeldung.lss.security" })
@EnableWebSecurity
public class LssSecurityConfig {

    @Autowired
    private LssSecurityAuthenticationDetailsSource authenticationDetailsSource;
    @Autowired
    private LssUsernamePasswordAuthenticationProvider authenticationProvider;

    public LssSecurityConfig() {
        super();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {// @formatter:off
        http
        .authorizeRequests()
                .antMatchers("/signup", "/user/register", "/code*").permitAll()
                .anyRequest().authenticated()

        .and()
        .formLogin().
            loginPage("/login").permitAll().
            loginProcessingUrl("/doLogin")
            .authenticationDetailsSource(authenticationDetailsSource)

        .and()
        .logout().permitAll().logoutUrl("/logout")

        .and()
        .csrf().disable();
        return http.build();
    } // @formatter:on

}
