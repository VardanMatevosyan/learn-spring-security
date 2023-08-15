package com.baeldung.lss.validation.impl;

import com.baeldung.lss.validation.ValidPassword;
import com.google.common.base.Joiner;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.Rule;
import org.passay.RuleResult;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

  @Override
  public void initialize(ValidPassword constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(String password, ConstraintValidatorContext context) {
    var passwordValidator = getPasswordValidator();
    var validatedResults = passwordValidator.validate(new PasswordData(password));
    if (validatedResults.isValid()) {
      return true;
    }
    addConstraintMessages(context, passwordValidator, validatedResults);
    return false;
  }

  private static PasswordValidator getPasswordValidator() {
    return new PasswordValidator(getPasswordValidationRules());
  }

  private static List<Rule> getPasswordValidationRules() {
    return List.of(new LengthRule(8, 30));
  }

  private static void addConstraintMessages(ConstraintValidatorContext context,
                                            PasswordValidator passwordValidator,
                                            RuleResult validatedResults) {
    var message = buildConstrainViolationMessage(passwordValidator, validatedResults);
    var constraintBuilder = context.buildConstraintViolationWithTemplate(message);
    constraintBuilder.addConstraintViolation();
  }

  private static String buildConstrainViolationMessage(PasswordValidator passwordValidator, RuleResult validatedResults) {
    return Joiner.on("n").join(passwordValidator.getMessages(validatedResults));
  }
}
