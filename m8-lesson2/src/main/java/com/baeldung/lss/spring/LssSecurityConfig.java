package com.baeldung.lss.spring;

import com.baeldung.lss.security.LssUserDetailsService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.baeldung.lss.model.User;
import com.baeldung.lss.persistence.UserRepository;
import com.baeldung.lss.security.CustomAuthenticationProvider;

import jakarta.annotation.PostConstruct;

@EnableWebSecurity
@Configuration
public class LssSecurityConfig {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;

    @Autowired
    private LssUserDetailsService userDetailsService;

    private PasswordEncoder passwordEncoder;

    public LssSecurityConfig(PasswordEncoder passwordEncoder) {
        super();
        this.passwordEncoder = passwordEncoder;
    }

    //

    @PostConstruct
    private void saveTestUser() {
        final User user = new User();
        user.setEmail("test@email.com");
        user.setPassword(passwordEncoder.encode("pass"));
        userRepository.save(user);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {// @formatter:off
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);

        // Usually it is not required to create new provider manager with lis of providers
        // but in very advance cases it may be the solution when we need to have full control
        // and Spring Security provide us this possibility to do it.
        ProviderManager providerManager = new ProviderManager(List.of(customAuthenticationProvider, daoAuthenticationProvider));
        providerManager.setEraseCredentialsAfterAuthentication(false);
        auth.parentAuthenticationManager(providerManager);
        // also we can configure EraseCredentialsAfterAuthentication through the AuthenticationManagerBuilder object
    } // @formatter:on

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {// @formatter:off
        http
        .authorizeHttpRequests()
                .requestMatchers("/badUser*","/js/**").permitAll()
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
    } // @formatter:on
}
