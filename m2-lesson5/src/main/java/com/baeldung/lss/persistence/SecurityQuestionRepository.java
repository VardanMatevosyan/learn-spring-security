package com.baeldung.lss.persistence;

import com.baeldung.lss.model.SecurityQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SecurityQuestionRepository extends JpaRepository<SecurityQuestion, Long> {

  SecurityQuestion findBySecurityQuestionDefinitionIdAndUserIdAndAnswer(Long questionDefinitionId, Long userId, String answer);
  SecurityQuestion findByUserIdAndAnswer(Long userId, String answer);

}
