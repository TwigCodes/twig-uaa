package com.twigcodes.uaa.web.rest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@FrameworkEndpoint
public class TokenResource {
  @Resource(name = "tokenServices")
  ConsumerTokenServices tokenServices;

  @Resource(name = "tokenStore")
  TokenStore tokenStore;

  @PostMapping("/oauth/token/revokeById/{tokenId}")
  @ResponseBody
  public void revokeToken(HttpServletRequest request, @PathVariable String tokenId) {
    tokenServices.revokeToken(tokenId);
  }

  @GetMapping("/tokens")
  public List<String> getTokens() {
    List<String> tokenValues = new ArrayList<String>();
    Collection<OAuth2AccessToken> tokens = tokenStore.findTokensByClientId("sampleClientId");
    if (tokens != null) {
      for (OAuth2AccessToken token : tokens) {
        tokenValues.add(token.getValue());
      }
    }
    return tokenValues;
  }

  @PostMapping("/tokens/revokeRefreshToken/{tokenId:.*}")
  public String revokeRefreshToken(@PathVariable String tokenId) {
    if (tokenStore instanceof JdbcTokenStore) {
      ((JdbcTokenStore) tokenStore).removeRefreshToken(tokenId);
    }
    return tokenId;
  }
}
