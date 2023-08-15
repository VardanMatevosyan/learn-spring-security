package com.baeldung.lss.service;

import com.baeldung.lss.validation.EmailExistsException;
import com.baeldung.lss.web.model.User;
import com.baeldung.lss.web.model.VerificationToken;

public interface IUserService {

    User registerNewUser(User user) throws EmailExistsException;

    User updateExistingUser(User user) throws EmailExistsException;

    void createVerificationToken(User user, String token);

    VerificationToken getVerificationToken(String token);

    void saveRegisteredUser(User user);
}
