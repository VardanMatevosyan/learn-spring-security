package com.baeldung.lss.web.event.listener;

import com.baeldung.lss.service.IUserService;
import com.baeldung.lss.web.event.model.OnRegistrationCompleteEvent;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

  private final IUserService userService;
  private final MessageSource messageSource;
  private final JavaMailSender mailSender;


  @Autowired
  public RegistrationListener(IUserService userService,
                                  MessageSource messageSource,
                                  JavaMailSender mailSender) {
    this.userService = userService;
    this.messageSource = messageSource;
    this.mailSender = mailSender;
  }

  @Override
  public void onApplicationEvent(OnRegistrationCompleteEvent event) {
    confirmRegistration(event);
  }

  private void confirmRegistration(OnRegistrationCompleteEvent event) {
    String token = UUID.randomUUID().toString();
    createVerificationToken(event, token);
    sendConfirmationEmail(event, token);
  }

  private void createVerificationToken(OnRegistrationCompleteEvent event, String token) {
    userService.createVerificationToken(event.getUser(), token);
  }

  private void sendConfirmationEmail(OnRegistrationCompleteEvent event, String token) {
    String subject = "Registration Confirmation";
    String baseUrl = "http://localhost:8081";
    String confirmationUrl = baseUrl + event.getAppUrl() + "/registrationConfirmation?token=" + token;


    String message = messageSource.getMessage("registration.success", null, event.getLocale());

    SimpleMailMessage mailMessage = new SimpleMailMessage();
    mailMessage.setFrom("vardanmt@gmail.com");
    mailMessage.setTo(event.getUser().getEmail());
    mailMessage.setSubject(subject);
    mailMessage.setText(message + "\r\n" + confirmationUrl);
    mailSender.send(mailMessage);
  }
}
