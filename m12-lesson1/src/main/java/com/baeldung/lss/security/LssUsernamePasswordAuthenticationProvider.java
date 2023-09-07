package com.baeldung.lss.security;

import com.baeldung.lss.persistence.UserRepository;
import com.baeldung.lss.web.model.User;
import java.util.Arrays;
import java.util.Collection;
import javax.transaction.Transactional;
import org.jboss.aerogear.security.otp.Totp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class LssUsernamePasswordAuthenticationProvider implements AuthenticationProvider {

  private static final String ROLE_USER = "ROLE_USER";

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private PasswordEncoder encoder;

  @Override
  public Authentication authenticate(Authentication auth) throws AuthenticationException {
    User user = userRepository.findByEmail(auth.getName());
    validateUser(user, auth);
    verifyOneTimePassword(user.getSecret(), auth);
    return new UsernamePasswordAuthenticationToken(user, getPassword(auth), getAuthorities());
  }

  @Override
  public boolean supports(Class<?> auth) {
    return auth.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
  }

  private void validateUser(User user, Authentication auth) {
    if (user == null) {
      throw new UsernameNotFoundException("No user found with username: " + auth.getName());
    } else if (!encoder.matches(getPassword(auth), user.getPassword())) {
      throw new BadCredentialsException("Invalid username or password");
    }
  }

  private void verifyOneTimePassword(String secret, Authentication auth) {
    String verificationCode = getVerificationCode(auth);
    Totp totp = new Totp(secret);
    try {
      if (!totp.verify(verificationCode)) {
        throw new BadCredentialsException("Invalid verification code");
      }
    } catch (Exception e) {
      throw new BadCredentialsException("Invalid verification code");
    }
  }

  private static String getVerificationCode(Authentication auth) {
    return ((LssSecurityWebAuthenticationDetails) auth.getDetails())
        .getVerificationCode();
  }

  private Collection<? extends GrantedAuthority> getAuthorities() {
    return Arrays.asList(new SimpleGrantedAuthority(ROLE_USER));
  }

  private static String getPassword(Authentication auth) {
    return auth.getCredentials().toString();
  }
}