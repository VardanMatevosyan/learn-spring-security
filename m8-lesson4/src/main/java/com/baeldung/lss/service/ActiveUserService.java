package com.baeldung.lss.service;

import com.baeldung.lss.model.User;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;

import org.springframework.stereotype.Service;

@Service
public class ActiveUserService {

  @Autowired
  private SessionRegistry sessionRegistry;

  @Autowired
  private UserService userService;

  public List<User> getAllActiveUsers() {
    return Stream.of(getAllPrincipalUsers())
        .filter(u -> !sessionRegistry.getAllSessions(u, false).isEmpty())
        .map(u -> userService.findUserByEmail(u.getUsername()))
        .toList();
  }

  private org.springframework.security.core.userdetails.User[] getAllPrincipalUsers() {
    List<Object> allPrincipals = sessionRegistry.getAllPrincipals();
    return allPrincipals.toArray(
        new org.springframework.security.core.userdetails.User[allPrincipals.size()]);
  }

}
