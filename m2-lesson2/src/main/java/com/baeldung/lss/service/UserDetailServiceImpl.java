package com.baeldung.lss.service;


import com.baeldung.lss.persistence.UserRepository;
import com.baeldung.lss.web.model.User;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    return buildUserDetails(findUser(email));
  }

  private User findUser(String email) {
    return Optional.ofNullable(userRepository.findByEmail(email))
        .orElseThrow(getUsernameNotFoundExceptionSupplier(email));
  }

  private UserDetails buildUserDetails(User user) {
    return new org.springframework.security.core.userdetails.User(
        user.getEmail(),
        user.getPassword(),
        true,
        true,
        true,
        true,
        buildAuthorities("ROLE_USER")
    );
  }

  private Collection<? extends GrantedAuthority> buildAuthorities(String ... roles) {
    return Arrays
        .stream(roles)
        .map(SimpleGrantedAuthority::new)
        .toList();
  }

  private Supplier<UsernameNotFoundException> getUsernameNotFoundExceptionSupplier(String email) {
    return () -> new UsernameNotFoundException("Not found user by username %s".formatted(email));
  }
}
