package com.baeldung.lss.registration.event;

import com.baeldung.lss.model.User;
import org.springframework.context.ApplicationEvent;

public class OnPasswordResetTokenEvent extends ApplicationEvent {

  private final String appUrl;
  private final String token;
  private final User user;

  public OnPasswordResetTokenEvent(User user, String appUrl, String token) {
    super(user);
    this.user = user;
    this.token = token;
    this.appUrl = appUrl;
  }

  public String getToken() {
    return token;
  }

  public String getAppUrl() {
    return appUrl;
  }

  public User getUser() {
    return user;
  }
}
