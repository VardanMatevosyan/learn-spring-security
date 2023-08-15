package com.baeldung.lss.web.event.model;

import com.baeldung.lss.web.model.User;
import java.util.Locale;
import org.springframework.context.ApplicationEvent;

public class OnRegistrationCompleteEvent extends ApplicationEvent {

  private User user;
  private String appUrl;
  private Locale locale;

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public String getAppUrl() {
    return appUrl;
  }

  public void setAppUrl(String appUrl) {
    this.appUrl = appUrl;
  }

  public Locale getLocale() {
    return locale;
  }

  public void setLocale(Locale locale) {
    this.locale = locale;
  }

  public OnRegistrationCompleteEvent(User user, String appUrl, Locale locale) {
    super(user);
    this.user = user;
    this.appUrl = appUrl;
    this.locale = locale;
  }
}
