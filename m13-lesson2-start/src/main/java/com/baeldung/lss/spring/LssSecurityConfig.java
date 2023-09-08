package com.baeldung.lss.spring;

import com.baeldung.lss.security.LssAuthenticationDetailSource;
import com.baeldung.lss.security.LssDaoAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class LssSecurityConfig {

    @Autowired
    private LssDaoAuthenticationProvider daoAuthenticationProvider;

    @Autowired
    private LssAuthenticationDetailSource lssAuthenticationDetailSource;

    public LssSecurityConfig() {
        super();
    }

    //

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {// @formatter:off
        auth.authenticationProvider(daoAuthenticationProvider);
    } // @formatter:on

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {// @formatter:off
        http
        .authorizeRequests()
                .antMatchers("/badUser*","/js/**").permitAll()
                .anyRequest().authenticated()

        .and()
        .formLogin().
            loginPage("/login").permitAll().
            loginProcessingUrl("/doLogin")
            .authenticationDetailsSource(lssAuthenticationDetailSource)

        .and()
        .logout().permitAll().logoutUrl("/logout")

        .and()
        .csrf().disable()
        ;
        return http.build();
    } // @formatter:on


}
