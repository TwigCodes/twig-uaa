package com.twigcodes.uaa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/** 配置中心服务器 */
@SpringBootApplication
@EnableDiscoveryClient
@RefreshScope
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
