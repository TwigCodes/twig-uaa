package com.twigcodes.uaa.config.security;

import com.twigcodes.commons.problem.InternalServerErrorException;
import com.twigcodes.uaa.config.Constants;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.xml.bind.DatatypeConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
@RequiredArgsConstructor
public class JdSmartOAuth2Util {
  private static final String PLATFORM = "ios";
  private static final String ANDROID_PACKAGE_NAME = "com.sc.smarthome";
  private static final String ANDROID_SHA1_SIGNATURE = "C7DAB911032E9E6CD2FBAB01F324A9B37D452F8B";
  private static final String IOS_BUNDLE_ID = "com.sc.smart";
  public static String getJdAuthUrl(String clientId, String authorizationUri) {
    // Get current date time
    val formatDateTime = getFormattedDate();
    log.debug("用于生成京东智能云的 OAuth 鉴权地址的时间戳是： {}", formatDateTime);
    val requestParams = new HashMap<String, String>();
    try {
//      val signature = buildAppSignature(ANDROID_SHA1_SIGNATURE, clientId, formatDateTime);
      val identity = buildAppSignature(IOS_BUNDLE_ID, clientId, formatDateTime);

//      requestParams.put("signature", signature);
      requestParams.put("identity", identity);
      requestParams.put("plat", PLATFORM);
      requestParams.put("timestamp", formatDateTime);
      return requestParams
        .keySet()
        .stream()
        .map(key -> String.format("%s=%s", key, requestParams.get(key)))
        .collect(
          Collectors.joining(
            "&",
            authorizationUri + "?",
            ""));
    } catch (NoSuchAlgorithmException e) {
      log.debug("算法没有找到，抛出异常", e.getMessage());
      throw new InternalServerErrorException("算法没有找到，抛出异常");
    }
  }
  private static String buildAppSignature(String toBeHashed, String appKey, String timestamp)
    throws NoSuchAlgorithmException {
    val timeStampedSignature = toBeHashed + timestamp + appKey + timestamp;
    val digestedSignatureResult = buildDigest(timeStampedSignature, true);
    log.debug("digestedSignatureResult: {}", digestedSignatureResult);
    return digestedSignatureResult;
  }

  private static String buildDigest(String token, boolean isUpperCase)
    throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance("MD5");
    md.update(token.getBytes());
    byte[] digest = md.digest();
    return isUpperCase
      ? DatatypeConverter.printHexBinary(digest).toUpperCase()
      : DatatypeConverter.printHexBinary(digest).toLowerCase();
  }

  private static String getFormattedDate() {
    val formatter =
      DateTimeFormatter.ofPattern(Constants.DEFAULT_TIME_STAMP_FORMAT)
        .withZone(ZoneId.of(Constants.DEFAULT_TIME_ZONE));
    return formatter.format(Instant.now());
  }

  private static Optional<String> encodeValue(String value) {
    try {
      return Optional.of(URLEncoder.encode(value, StandardCharsets.UTF_8.toString()));
    } catch (UnsupportedEncodingException e) {
      return Optional.empty();
    }
  }
}
