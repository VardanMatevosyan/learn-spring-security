package com.baeldung.lss.security;

import com.baeldung.lss.model.IEntity;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

@Service
@Transactional
public class LssAclPermissionService {

  private final MutableAclService aclService;
  private final PlatformTransactionManager transactionManager;

  @Autowired
  public LssAclPermissionService(MutableAclService aclService,
                                PlatformTransactionManager transactionManager) {
    this.aclService = aclService;
    this.transactionManager = transactionManager;
  }

  public void addUserPermission(IEntity targetEntity, Permission permission, String username) {
    Sid sid = new PrincipalSid(username);
    addSidPermissionInTx(targetEntity, permission, sid);
  }


  public void addRolePermission(IEntity targetEntity, Permission permission, String username) {
    Sid sid = new GrantedAuthoritySid(username);
    addSidPermission(targetEntity, permission, sid);
  }


  private void addSidPermission(IEntity targetEntity, Permission permission, Sid sid) {
    var objectIdentity = new ObjectIdentityImpl(targetEntity.getClass(), targetEntity.getId());
    MutableAcl acl;

    try {
      acl = (MutableAcl) aclService.readAclById(objectIdentity);
    } catch (NotFoundException e) {
      acl =  aclService.createAcl(objectIdentity);
    }

    acl.insertAce(acl.getEntries().size(), permission, sid, true);
    aclService.updateAcl(acl);
  }


  private void addSidPermissionInTx(IEntity targetObj, Permission permission, Sid sid) {
    final TransactionTemplate tt = new TransactionTemplate(transactionManager);

    tt.execute(new TransactionCallbackWithoutResult() {
      @Override
      protected void doInTransactionWithoutResult(TransactionStatus status) {
        final ObjectIdentity oi = new ObjectIdentityImpl(targetObj.getClass(), targetObj.getId());

        MutableAcl acl = null;
        try {
          acl = (MutableAcl) aclService.readAclById(oi);
        } catch (final NotFoundException nfe) {
          acl = aclService.createAcl(oi);
        }

        acl.insertAce(acl.getEntries()
            .size(), permission, sid, true);
        aclService.updateAcl(acl);
      }
    });
  }
}
