package com.baeldung.lss.security;

import com.baeldung.lss.persistence.UserRepository;
import com.baeldung.lss.web.model.Role;
import com.baeldung.lss.web.model.User;
import java.util.Collection;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LssUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Autowired
  public LssUserDetailsService(UserRepository userRepository) {
    super();
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = findUser(username);
    return getPrincipalUser(user);
  }

  private org.springframework.security.core.userdetails.User getPrincipalUser(User user) {
    return new org.springframework.security.core.userdetails.User(
        user.getEmail(),
        user.getPassword(),
        true,
        true,
        true,
        true,
        getAuthorities(user.getRoles())
    );
  }

  private Collection<? extends GrantedAuthority> getAuthorities(Set<Role> roles) {
    return roles.stream()
        .flatMap(role -> role.getPrivileges().stream())
        .map(privilege -> new SimpleGrantedAuthority(privilege.getName()))
        .toList();
  }

  private User findUser(String username) {
    return userRepository
        .findByEmail(username)
        .orElseThrow(() ->
            new UsernameNotFoundException("User not found by username %s".formatted(username)));
  }
}
