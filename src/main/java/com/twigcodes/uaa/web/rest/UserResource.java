package com.twigcodes.uaa.web.rest;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

/**
 * UserResource
 */
@RestController
public class UserResource {

    @GetMapping(value="/user")
    public Principal getUser(Principal user) {
        return user;
    }
}
