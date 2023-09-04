package com.baeldung.lss.spring.vouter;

import com.baeldung.lss.spring.vouter.util.LockedUsersUtil;
import java.util.Collection;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;

public class RuntimeLockUserVoter implements AccessDecisionVoter<Object> {

  @Override
  public boolean supports(ConfigAttribute attribute) {
    return true;
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return true;
  }

  @Override
  public int vote(Authentication authentication,
                  Object object,
                  Collection<ConfigAttribute> attributes) {
    if (LockedUsersUtil.isLocked(authentication.getName())) {
      return ACCESS_DENIED;
    }
    return ACCESS_GRANTED;
  }
}
