package com.baeldung.lss.web.controller;

import com.baeldung.lss.service.IUserService;
import com.baeldung.lss.validation.EmailExistsException;
import com.baeldung.lss.web.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RegistrationController {

  @Autowired
  private IUserService userService;

  // displaying the registration form page
  @RequestMapping(value = "signup")
  public ModelAndView registrationForm() {
    return new ModelAndView("registrationPage", "user", new User());
  }

  // registering user logic
  @RequestMapping(value = "user/register")
  public ModelAndView registerUser(User user, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return new ModelAndView("registrationPage", "user", user);
    }

    try {
      userService.registerNewUser(user);
    } catch (EmailExistsException e) {
      bindingResult.addError(new FieldError("user", "email", e.getMessage()));
      return new ModelAndView("registrationPage", "user", user);
    }
    return new ModelAndView("redirect:/login");
  }

}
