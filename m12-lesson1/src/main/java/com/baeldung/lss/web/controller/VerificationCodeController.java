package com.baeldung.lss.web.controller;

import static java.lang.String.format;
import static java.util.Objects.nonNull;

import com.baeldung.lss.persistence.UserRepository;
import com.baeldung.lss.web.model.User;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VerificationCodeController {

  public static String QR_PREFIX = "https://chart.googleapis.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=";
  public static String APP_NAME = "LearnSpringSecurity";

  private final UserRepository userRepository;

  @Autowired
  public VerificationCodeController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @RequestMapping(value = "/code", method = RequestMethod.GET)
  public Map<String, String> getQrCodeUrl(@RequestParam String username) throws UnsupportedEncodingException {
    Map<String, String> result = new HashMap<>();
    User user = userRepository.findByEmail(username);

    if (nonNull(user)) {
      result.put("url", generateQrCodeUrl(user.getSecret(), user.getEmail()));
    } else {
      result.put("url", "");
    }

    return result;
  }

  private String generateQrCodeUrl(String secret, String username) throws UnsupportedEncodingException {
    return QR_PREFIX + URLEncoder.encode(
        format("otpauth://totp/%s:%s?secret=%s&issuer=%s", APP_NAME, username, secret, APP_NAME),
        "UTF-8");
  }

}
