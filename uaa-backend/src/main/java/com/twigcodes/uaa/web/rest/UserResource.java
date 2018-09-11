package com.twigcodes.uaa.web.rest;

import java.security.Principal;

import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.web.bind.annotation.GetMapping;

/** UserResource */
@FrameworkEndpoint
public class UserResource {

  @GetMapping("/user")
  public Principal getUser(Principal user) {
    return user;
  }
}
