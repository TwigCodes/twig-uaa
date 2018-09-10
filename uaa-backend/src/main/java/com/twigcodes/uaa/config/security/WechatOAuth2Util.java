package com.twigcodes.uaa.config.security;

import java.util.HashMap;
import java.util.stream.Collectors;
import lombok.val;

public class WechatOAuth2Util {
  public static String getWechatAuthUrl(String clientId, String authorizationUri) {
    val requestParams = new HashMap<String, String>();
    requestParams.put("appid", clientId);
    return requestParams
      .keySet()
      .stream()
      .map(key -> key + "=" + requestParams.get(key))
      .collect(
        Collectors.joining(
          "&",
          authorizationUri + "?",
          ""));
  }
}
