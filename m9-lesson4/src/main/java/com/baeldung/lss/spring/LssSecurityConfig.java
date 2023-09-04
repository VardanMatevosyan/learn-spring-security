package com.baeldung.lss.spring;

import com.baeldung.lss.spring.vouter.RuntimeLockUserVoter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionVoter;

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

            .antMatchers("/secured").hasRole("ADMIN")
            .anyRequest().authenticated().accessDecisionManager(unanimousAccessDecisionManager())

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

    @Bean
    public AccessDecisionManager unanimousAccessDecisionManager() {
        return new UnanimousBased(getAccessDecisionVoters());
    }

    private static List<AccessDecisionVoter<?>> getAccessDecisionVoters() {
    return List.of(
        new RoleVoter(),
        new AuthenticatedVoter(),
        new WebExpressionVoter(),
        new RuntimeLockUserVoter());
    }



}
