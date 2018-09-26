package com.twigcodes.uaa.web.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
public class HelloResource {

  @GetMapping("/hello")
  public String hello(@RequestParam String name) {
    return "Hello" + name;
  }
}
