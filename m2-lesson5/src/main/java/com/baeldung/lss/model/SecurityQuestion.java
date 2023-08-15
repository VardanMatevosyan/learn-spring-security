package com.baeldung.lss.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class SecurityQuestion {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;


  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id", nullable = false, unique = true)
  private User user;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "securityQuestionDefinition_id", nullable = false)
  private SecurityQuestionDefinition securityQuestionDefinition;

  private String answer;


  public SecurityQuestion() {
  }

  public SecurityQuestion(User user, SecurityQuestionDefinition securityQuestionDefinition, String answer) {
    this.user = user;
    this.securityQuestionDefinition = securityQuestionDefinition;
    this.answer = answer;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public SecurityQuestionDefinition getSecurityQuestionDefinition() {
    return securityQuestionDefinition;
  }

  public void setSecurityQuestionDefinition(
      SecurityQuestionDefinition securityQuestionDefinition) {
    this.securityQuestionDefinition = securityQuestionDefinition;
  }

  public String getAnswer() {
    return answer;
  }

  public void setAnswer(String answer) {
    this.answer = answer;
  }
}
