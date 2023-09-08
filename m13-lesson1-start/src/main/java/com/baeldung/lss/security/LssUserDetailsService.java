package com.baeldung.lss.security;

import static java.util.Objects.isNull;

import com.baeldung.lss.service.UserServiceInterface;
import java.util.Collection;
import java.util.Collections;
import javax.ejb.EJB;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class LssUserDetailsService implements UserDetailsService {

  @EJB(mappedName = "java:module/userService")
  UserServiceInterface userService;

  public LssUserDetailsService() {
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    com.baeldung.lss.model.User lssUser = userService.findByEmail(username);
    if (isNull(lssUser)) {
      throw new UsernameNotFoundException("User not found by email " + username);
    }
    return new User(lssUser.getEmail(), lssUser.getPassword(), getAuthorities());
  }

  private Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
  }
}
