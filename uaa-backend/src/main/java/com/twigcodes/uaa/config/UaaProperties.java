package com.twigcodes.uaa.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "uaa")
public class UaaProperties {
  private Security security = new Security();
  private final JdSmart jdSmart = new JdSmart();

  @Data
  public static class JdSmart {
    private String androidPackageName;
    private String androidSha1Signature;
    private String iOSBundleId;
  }

  @Data
  public static class Security {
    private final Jwt jwt = new Jwt();
    private final Authorization authorization = new Authorization();
    private final OAuth2 oAuth2 = new OAuth2();

    @Data
    public static class Authorization {
      private String header = "Authorization";
    }

    @Data
    public static class Jwt {
      private String secret = "mySecret";
      private long tokenValidityInSeconds = 7200;
      private long refreshTokenValidityInSeconds = 2592000;
      private String tokenPrefix = "Bearer ";
    }

    @Data
    public static class OAuth2 {
      private String serverUrl = "http://localhost:8095";
      private String authorizeUrl = serverUrl + "/oauth/authorize";
      private String clientId = "discoveryClient";
      private String clientSecret = "discoverySecret ";
    }
  }
}
