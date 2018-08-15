package com.twigcodes.uaa.config;

/**
 * 应用的常量，需要在全局可用的常量都应该在这个类中定义
 *
 * @author Peng Wang (wpcfan@gmail.com)
 */
public final class Constants {

    // Spring profiles for development, test and production
    public static final String SPRING_PROFILE_DEVELOPMENT = "dev";
    public static final String SPRING_PROFILE_TEST = "test";
    public static final String SPRING_PROFILE_PRODUCTION = "prod";

    // Regular Expressionss
    // The regular expression to evaluate username
    public static final String REGEX_LOGIN = "^[_'.@A-Za-z0-9-]*$";
    // The regular expression to evaluate user's mobile
    public static final String REGEX_MOBILE = "^1[3456789]\\d{9}$";
    // The regular expression to a Chinese Citizen ID Card number
    public static final String REGEX_ID = "^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";


    public static final String ANONYMOUS_USER = "anonymoususer";
    public static final String DEFAULT_LANGUAGE = "zh-cn";

    public static final String BASE_PACKAGE_NAME = "com.twigcodes.uaa";

    // Caches
    // The cache to store the state passed by Jd Smart Cloud callback
    public static final String CACHE_JDSMART_AUTH_STATE_NAME = "JdSmartStateCache";
    // The cache to store LeChange admin token
    public static final String CACHE_LECHANGE_ACCESS_TOKEN_NAME = "LeChangeAdminAccessTokenCache";
    // The key to get LeChange admin token
    public static final String CACHE_LECHANGE_ACCESS_TOKEN_KEY = "LeChangeAdminAccessToken";
    // The cache to store LeChange user account, which is his/her mobile number
    public static final String CACHE_LECHANGE_USER_TOKEN_NAME = "LeChangeUserTokenCache";
    // The cache to store LeanCloud Captcha's validate_token
    public static final String CACHE_LEANCLOUD_CAPTCHA_NAME = "CaptchaValidatedTokenCache";
    public static final String CACHE_JDSMART_AUTH_TOKEN_NAME = "JdSmartAuthTokenCache";
    public static final String DEFAULT_TIME_ZONE = "Asia/Shanghai";
    public static final String DEFAULT_TIME_STAMP_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private Constants() {
    }
}
