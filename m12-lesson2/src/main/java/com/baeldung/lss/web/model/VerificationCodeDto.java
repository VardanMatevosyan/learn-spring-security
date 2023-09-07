package com.baeldung.lss.web.model;

import java.util.List;

public class VerificationCodeDto {

  public VerificationCodeDto() {
  }

  private String from;
  private List<String> to;
  private String body;

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public List<String> getTo() {
    return to;
  }

  public void setTo(List<String> to) {
    this.to = to;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }
}
