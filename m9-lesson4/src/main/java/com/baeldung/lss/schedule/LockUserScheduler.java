package com.baeldung.lss.schedule;

import com.baeldung.lss.spring.vouter.util.LockedUsersUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class LockUserScheduler {

  public LockUserScheduler() {
  }

  private static final Logger LOG = LoggerFactory.getLogger(LockUserScheduler.class);

  @Scheduled(cron = "*/30 * * * * *")
  public void lock() {
    LOG.info("Trying to lock the user");
    LockedUsersUtil.lock("admin");
    LOG.info("The user is locked");

  }

  @Scheduled(cron = "*/40 * * * * *")
  public void unlock() {
    LOG.info("Trying to unlock the user");
    LockedUsersUtil.unlock("admin");
    LOG.info("The user is unlocked");
  }

}
