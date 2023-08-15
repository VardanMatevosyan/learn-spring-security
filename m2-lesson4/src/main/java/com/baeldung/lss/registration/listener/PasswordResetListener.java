package com.baeldung.lss.registration.listener;

import com.baeldung.lss.registration.event.OnPasswordResetTokenEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class PasswordResetListener implements ApplicationListener<OnPasswordResetTokenEvent> {

  @Autowired
  private JavaMailSender javaMailSender;
  @Autowired
  private Environment env;

  @Override
  public void onApplicationEvent(OnPasswordResetTokenEvent event) {
    sendResetPasswordEmail(event);
  }

  private void sendResetPasswordEmail(OnPasswordResetTokenEvent event) {
    javaMailSender.send(buildEmailMessage(event));
  }

  private SimpleMailMessage buildEmailMessage(OnPasswordResetTokenEvent event) {
    SimpleMailMessage email = new SimpleMailMessage();
    email.setFrom(env.getProperty("support.email"));
    email.setTo(event.getUser().getEmail());
    email.setSubject("Reset password");
    email.setText("Please open the following URL to reset your password: \r\n" + getUrl(event));
    return email;
  }

  private static String getUrl(OnPasswordResetTokenEvent event) {
    return event.getAppUrl() + "/user/changePassword?token=" + event.getToken();
  }
}
