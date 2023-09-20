package com.baeldung.lsso.spring;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "oauth2")
public class AuthServerConfig {

  private final Map<String, String> issuersJwkSetUri = new HashMap<>();

  public AuthServerConfig(Map<String, AuthServer> authServers) {
    authServers
        .values()
        .forEach(authServer ->
            issuersJwkSetUri.put(authServer.getIssuerUri(), authServer.getJwkSetUrl()));
  }

  public Map<String, String> getIssuersJwkSetUri() {
    return issuersJwkSetUri;
  }

  public Set<String> getTrustedIssuerUris() {
    return issuersJwkSetUri.keySet();
  }

  public static class AuthServer {
    private String jwkSetUrl;
    private String issuerUri;

    public String getJwkSetUrl() {
      return jwkSetUrl;
    }

    public void setJwkSetUrl(String jwkSetUrl) {
      this.jwkSetUrl = jwkSetUrl;
    }

    public String getIssuerUri() {
      return issuerUri;
    }

    public void setIssuerUri(String issuerUri) {
      this.issuerUri = issuerUri;
    }
  }



}
