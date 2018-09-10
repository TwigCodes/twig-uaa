package com.twigcodes.uaa.config.security;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.oauth2.resource.FixedPrincipalExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;

@Slf4j
public class PrincipalExtractorBuilder {

  private static final String GITHUB = "github";
  private static final String FACEBOOK = "facebook";

  public PrincipalExtractor build(String type) {
    PrincipalExtractor extractor;
    switch (type) {
      case GITHUB:
        extractor = new GitHubExtractor();
        break;
      case FACEBOOK:
        extractor = new FacebookExtractor();
        break;
      default:
        extractor = new FixedPrincipalExtractor();
        break;
    }
    return extractor;
  }

  private class GitHubExtractor implements PrincipalExtractor {

    @Override
    public Object extractPrincipal(Map<String, Object> map) {
      return map.get("name");
    }
  }

  private class FacebookExtractor implements PrincipalExtractor {

    private String[] PRINCIPAL_KEYS =
        new String[] {"user", "username", "userid", "user_id", "login", "id", "name"};

    @Override
    public Object extractPrincipal(Map<String, Object> map) {
      for (String key : PRINCIPAL_KEYS) {
        if (map.containsKey(key)) {
          log.debug("[Facebook] returned key {}, value {}", key, map.get(key));
          return map.get(key);
        }
      }
      return null;
    }
  }
}
