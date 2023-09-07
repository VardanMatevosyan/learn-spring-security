package com.baeldung.lss.web.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Calendar;

import com.baeldung.lss.validation.PasswordMatches;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import org.jboss.aerogear.security.otp.api.Base32;

@Entity
@PasswordMatches
public class User {

    public User() {
        this.secret = Base32.random();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @NotEmpty(message = "Email is required.")
    private String email;

    @NotEmpty(message = "Password is required.")
    private String password;

    @NotEmpty(message = "Password confirmation is required.")
    private String passwordConfirmation;

    private String secret;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    private Calendar created = Calendar.getInstance();

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Calendar getCreated() {
        return this.created;
    }

    public void setCreated(Calendar created) {
        this.created = created;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(final String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", email='" + email + '\'' + ", password='" + password + '\'' + ", passwordConfirmation='" + passwordConfirmation + '\'' + ", created=" + created + '}';
    }
}
