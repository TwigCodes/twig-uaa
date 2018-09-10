package com.twigcodes.uaa.domain;

import com.twigcodes.uaa.config.security.JdSmartOAuth2Util;
import com.twigcodes.uaa.config.security.WechatOAuth2Util;
import com.twigcodes.uaa.domain.converter.StringSetConverter;
import java.util.Collections;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

@Data
@Entity
@Table(name = "twig_client_registration")
public class TwigClientRegistration {
  private static final String REGISTRATION_ID_JDSMART = "jdsmart";
  private static final String REGISTRATION_ID_WECHAT = "wechat";
  @Id
  @Column(name = "registration_id")
  private String registrationId;

  @Column(name = "client_id")
  private String clientId;

  @Column(name = "client_secret")
  private String clientSecret;

  @Column(name = "client_auth_method")
  private String clientAuthenticationMethod = "basic";

  @Column(name = "auth_grant_type")
  private String authorizationGrantType = "authorization_code";

  @Column(name = "redirect_uri")
  private String redirectUriTemplate;

  @Column(name = "scopes")
  @Convert(converter = StringSetConverter.class)
  private Set<String> scopes = Collections.emptySet();

  @Column(name = "authorization_uri")
  private String authorizationUri;

  @Column(name = "token_uri")
  private String tokenUri;

  @Column(name = "user_info_uri")
  private String userInfoUri;

  @Column(name = "user_info_attribute_name")
  private String userNameAttributeName;

  @Column(name = "jwk_set_uri")
  private String jwkSetUri;

  @Column(name = "client_name")
  private String clientName;

  public ClientRegistration toClientRegistration() {
    return ClientRegistration.withRegistrationId(this.getRegistrationId())
        .clientId(this.getClientId())
        .clientSecret(this.getClientSecret())
        .authorizationGrantType(new AuthorizationGrantType(this.getAuthorizationGrantType()))
        .clientAuthenticationMethod(new ClientAuthenticationMethod(this.getClientAuthenticationMethod()))
        .authorizationUri(this.buildAuthorizationUri())
        .tokenUri(this.getTokenUri())
        .jwkSetUri(this.getJwkSetUri())
        .userInfoUri(this.getUserInfoUri())
        .clientName(this.getClientName())
        .userNameAttributeName(this.getUserNameAttributeName())
        .scope(this.getScopes().toArray(new String[0]))
        .redirectUriTemplate(this.getRedirectUriTemplate())
        .build();
  }

  private String buildAuthorizationUri() {
    switch (registrationId) {
      case REGISTRATION_ID_JDSMART:
        return JdSmartOAuth2Util.getJdAuthUrl(clientId, authorizationUri);
      case REGISTRATION_ID_WECHAT:
        return WechatOAuth2Util.getWechatAuthUrl(clientId, authorizationUri);
      default:
        return this.getAuthorizationUri();
    }
  }
}
