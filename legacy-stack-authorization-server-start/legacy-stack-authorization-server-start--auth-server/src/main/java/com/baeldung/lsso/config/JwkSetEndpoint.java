package com.baeldung.lsso.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import java.security.KeyPair;
import java.security.Principal;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@FrameworkEndpoint
public class JwkSetEndpoint {

  private final KeyPair keyPair;

  @Autowired
  public JwkSetEndpoint(KeyPair keyPair) {
    this.keyPair = keyPair;
  }


  @GetMapping("/endpoint/jwks.json")
  @ResponseBody
  public Map<String, Object> getKeys(Principal principal) {
    RSAPublicKey publicKey = (RSAPublicKey) this.keyPair.getPublic();
    RSAKey rsaKey = new RSAKey.Builder(publicKey).build();
    return new JWKSet(rsaKey).toJSONObject();
  }

}
