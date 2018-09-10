package com.twigcodes.uaa.config.security;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Slf4j
public class PersistOAuth2UserService extends DefaultOAuth2UserService {
  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

    OAuth2User oath2User = super.loadUser(userRequest);
    return buildPrincipal(oath2User);
  }

  /**
   * Builds the security principal from the given userReqest. Registers the user if not already
   * reqistered
   */
  public OAuth2User buildPrincipal(OAuth2User oAuth2User) {
    Map<String, Object> attributes = oAuth2User.getAttributes();
    log.debug("OAuth2User info {}", attributes);
    return oAuth2User;
  }
}
