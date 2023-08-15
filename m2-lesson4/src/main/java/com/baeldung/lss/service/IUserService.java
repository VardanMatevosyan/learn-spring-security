package com.baeldung.lss.service;

import com.baeldung.lss.model.PasswordResetToken;
import com.baeldung.lss.model.User;
import com.baeldung.lss.model.VerificationToken;
import com.baeldung.lss.validation.EmailExistsException;

public interface IUserService {

    User findUserByEmail(final String email);

    User registerNewUser(User user) throws EmailExistsException;

    User updateExistingUser(User user) throws EmailExistsException;

    void createVerificationTokenForUser(User user, String token);

    VerificationToken getVerificationToken(String token);

    void saveRegisteredUser(User user);

  void createPasswordResetTokenForUser(User user, String token);

    void changeUserPassword(User user, String password);

    PasswordResetToken getPasswordResetToken(String token);
}
