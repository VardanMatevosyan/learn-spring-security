package com.baeldung.lss.persistence;

import com.baeldung.lss.model.Possession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PossessionRepository extends JpaRepository<Possession, Long> {

}
