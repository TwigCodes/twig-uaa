package com.twigcodes.uaa.web.rest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@FrameworkEndpoint
public class RevokeTokenEndpoint {

  @Resource(name = "tokenServices")
  ConsumerTokenServices consumerTokenServices;

  @DeleteMapping("/oauth/token")
  @ResponseBody
  public void revokeToken(HttpServletRequest request) {
    String authorization = request.getHeader("Authorization");
    if (authorization != null && authorization.contains("Bearer")) {
      String tokenId = authorization.substring("Bearer".length() + 1);
      consumerTokenServices.revokeToken(tokenId);
    }
  }
}
