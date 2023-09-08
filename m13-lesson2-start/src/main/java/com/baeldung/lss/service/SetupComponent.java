package com.baeldung.lss.service;

import com.baeldung.lss.model.User;
import com.baeldung.lss.validation.EmailExistsException;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SetupComponent {

    @Autowired
    private IUserService userService;

    @PostConstruct
    public void setup() throws EmailExistsException {
        this.userService.registerNewUser(new User("user@email.com", "pass", "Google"));
    }

}
