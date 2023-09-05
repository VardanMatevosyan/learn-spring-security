package com.baeldung.lss.web.controller;

import com.baeldung.lss.model.Possession;
import com.baeldung.lss.persistence.PossessionRepository;
import com.baeldung.lss.persistence.UserRepository;
import com.baeldung.lss.security.LssAclPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PossessionController {

  private final UserRepository userRepository;
  private final PossessionRepository possessionRepository;
  private final LssAclPermissionService lssAclPermissionService;

  @Autowired
  public PossessionController(UserRepository userRepository,
                              PossessionRepository possessionRepository,
                              LssAclPermissionService lssAclPermissionService) {
    this.userRepository = userRepository;
    this.possessionRepository = possessionRepository;
    this.lssAclPermissionService = lssAclPermissionService;
  }

  @RequestMapping(value = "/possessions/{id}", method = RequestMethod.GET)
  @ResponseBody
  @PostAuthorize("hasPermission(returnObject, 'READ') or hasPermission(returnObject, 'ADMINISTRATION')")
  public Possession findOne(@PathVariable("id") final Long id) {
    return possessionRepository.findById(id).orElse(null);
  }

  // this is just to test all the logic should be inside the separate service
  @RequestMapping(value = "/possessions", method = RequestMethod.POST)
  @ResponseBody
  public String create(@RequestBody Possession possession, Authentication authentication) {
    possession.setOwner(userRepository.findByEmail(authentication.getName()));
    possession = possessionRepository.save(possession);
    lssAclPermissionService.addUserPermission(possession, BasePermission.ADMINISTRATION, authentication.getName());
    return "Possession created with id " + possession.getId();
  }

}
