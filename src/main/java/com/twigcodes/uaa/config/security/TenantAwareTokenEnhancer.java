package com.twigcodes.uaa.config.security;

import java.util.HashMap;
import java.util.Map;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

/** 自定义 token 携带内容 */
public class TenantAwareTokenEnhancer implements TokenEnhancer {

  @Override
  public OAuth2AccessToken enhance(
      OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
    Map<String, Object> additionalInfo = new HashMap<String, Object>();
    // TODO: not implemented yet, will be implemented once we get tenant table design ready
    // We could inject the tenant info by authentication, and set tenant info into token then.
    additionalInfo.put("tenant", authentication.getName());
    ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
    return accessToken;
  }
}
