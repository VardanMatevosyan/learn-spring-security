package com.baeldung.lss.web.controller;

import static java.util.Objects.isNull;

import com.baeldung.lss.persistence.UserRepository;
import com.baeldung.lss.web.model.User;
import com.sinch.xms.ApiConnection;
import com.sinch.xms.SinchSMSApi;
import com.sinch.xms.api.MtBatchTextSmsCreate;
import com.sinch.xms.api.MtBatchTextSmsResult;
import java.io.IOException;
import org.jboss.aerogear.security.otp.Totp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestTemplate;

@Controller
public class VerificationCodeController {

  @Autowired
  private UserRepository userRepository;
//
//  @Autowired
//  private RestTemplate restTemplate;

  @Value("${sinch.sender}")
  private String senderNumber;
  @Value("${sinch.service.plan.id}")
  private String sinchServicePlanId;
  @Value("${sinch.token}")
  private String sinchToken;

  @RequestMapping(value = "/code", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  public void getCode(Authentication auth) throws IOException, InterruptedException {
    String name = auth.getName();
    User user = userRepository.findByEmail(name);
    if (isNull(user)) {
      return;
    }
    sendCode(user);
  }

  // this is for testing it should be in the separate service
  private void sendCode(User user) {
    String code = generateTotpCode(user);
    String body = "The verification code is " + code;
    try (ApiConnection conn = ApiConnection
        .builder()
        .servicePlanId(sinchServicePlanId)
        .token(sinchToken)
        .start()) {

      MtBatchTextSmsCreate message = SinchSMSApi
          .batchTextSms()
          .sender(senderNumber)
          .addRecipient(new String[]{user.getPhone()})
          .body(body)
          .build();

      MtBatchTextSmsResult batch = conn.createBatch(message);
      System.out.println("Body " + batch.body());
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  // send token using restTemplate to sinch api

//  private void sendCode(User user) throws JsonProcessingException {
//    String code = generateTotpCode(user);
//    String bodyCode = "The verification code is " + code;
//    String url = "https://sms.api.sinch.com/xms/v1/" + sinchServicePlanId + "/batches";
//    URI uri = URI.create(url);
//
//    VerificationCodeDto verificationCodeDto = new VerificationCodeDto();
//    verificationCodeDto.setFrom(senderNumber);
//    verificationCodeDto.setTo(List.of(user.getPhone()));
//    verificationCodeDto.setBody(bodyCode);
//
//    ObjectMapper objectMapper = new ObjectMapper();
//    String body = objectMapper.writeValueAsString(verificationCodeDto);
//
//    MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
//    headers.add("Authorization", "Bearer " + sinchToken);
//    headers.add("Content-Type", "application/json");
//
//    HttpEntity<String> request = new HttpEntity<>(body, headers);
//
//    restTemplate.postForEntity(uri, request, String.class);
//


//  }

  private static String generateTotpCode(User user) {
    return new Totp(user.getSecret()).now();
  }

}
