package com.baeldung.lss.security;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class LssPrincipalUser extends User {

  private String tenant;

    public LssPrincipalUser(String username,
      String password,
      boolean enabled,
      boolean accountNonExpired,
      boolean credentialsNonExpired,
      boolean accountNonLocked,
      Collection<? extends GrantedAuthority> authorities, String tenant) {
    super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    this.tenant = tenant;
  }

  public String getTenant() {
    return tenant;
  }

  public void setTenant(String tenant) {
      this.tenant = tenant;
  }
}
