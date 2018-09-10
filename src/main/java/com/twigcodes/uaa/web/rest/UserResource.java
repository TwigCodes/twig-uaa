package com.twigcodes.uaa.web.rest;

import java.security.Principal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/** UserResource */
@RestController
public class UserResource {

  @GetMapping(value = "/user")
  public Principal getUser(Principal user) {
    return user;
  }
}
