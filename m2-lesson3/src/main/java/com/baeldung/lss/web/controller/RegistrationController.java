package com.baeldung.lss.web.controller;

import com.baeldung.lss.web.event.model.OnRegistrationCompleteEvent;
import com.baeldung.lss.web.model.VerificationToken;
import groovy.util.logging.Slf4j;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.baeldung.lss.service.IUserService;
import com.baeldung.lss.validation.EmailExistsException;
import com.baeldung.lss.web.model.User;

import jakarta.validation.Valid;

@Controller
class RegistrationController {

    private final IUserService userService;
    private final MessageSource messageSource;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public RegistrationController(IUserService userService,
                                  MessageSource messageSource,
                                  ApplicationEventPublisher eventPublisher) {
        this.userService = userService;
        this.messageSource = messageSource;
        this.eventPublisher = eventPublisher;
    }

    //

    @RequestMapping(value = "signup")
    public ModelAndView registrationForm() {
        return new ModelAndView("registrationPage", "user", new User());
    }

    @RequestMapping(value = "user/register")
    public ModelAndView registerUser(@Valid final User user,
                                     final BindingResult result,
                                     final HttpServletRequest request) {
        if (result.hasErrors()) {
            return new ModelAndView("registrationPage", "user", user);
        }
        try {
            User registered = userService.registerNewUser(user);

            eventPublisher.publishEvent(
                new OnRegistrationCompleteEvent(registered, request.getContextPath(), request.getLocale()));

        } catch (EmailExistsException e) {
            result.addError(new FieldError("user", "email", e.getMessage()));
            return new ModelAndView("registrationPage", "user", user);
        }
        return new ModelAndView("redirect:/login");
    }


    @RequestMapping(value = "registrationConfirmation")
    public ModelAndView confirmRegistration(WebRequest request,
                                            ModelAndView model,
                                            @RequestParam("token") String token) {
        Locale locale = request.getLocale();

        VerificationToken verificationToken = userService.getVerificationToken(token);
        if (verificationToken == null) {
            String message = messageSource.getMessage("auth.invalidToken", null, locale);
            model.addObject("message", message);
            return new ModelAndView("badUser", "message", model);
        }

        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            String messageValue = messageSource.getMessage("auth.expired", null, locale);
            model.addObject("message", messageValue);
            return new ModelAndView("badUser", "message", model);
        }

        user.setEnabled(true);
        userService.saveRegisteredUser(user);
        System.out.println("confirm registration with token " + token);
        return new ModelAndView("loginPage");
    }

}
