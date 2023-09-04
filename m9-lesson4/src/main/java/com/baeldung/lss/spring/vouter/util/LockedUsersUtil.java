package com.baeldung.lss.spring.vouter.util;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class LockedUsersUtil {

  private static final Set<String> lockedUsers = new HashSet<>();
  private LockedUsersUtil() {
  }

  public static boolean isLocked(final String username) {
    return lockedUsers.contains(username);
  }

  public static void lock(final String username) {
    lockedUsers.add(username);
  }

  public static void unlock(final String username) {
    lockedUsers.remove(username);
  }

}
