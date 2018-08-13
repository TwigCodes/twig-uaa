package com.twigcodes.uaa.web.rest.exception;

import java.net.URI;

/**
 * 错误常量定义，使用 uri 形式唯一标识错误类型
 *
 * @author Peng Wang (wpcfan@gmail.com)
 */
final class ErrorConstants {

    public static final String ERR_CONCURRENCY_FAILURE = "error.concurrencyFailure";
    static final String ERR_VALIDATION = "error.validation";
    private static final String PROBLEM_BASE_URL = "http://www.twigcodes.com/problem";
    static final URI DEFAULT_TYPE = URI.create(PROBLEM_BASE_URL + "/problem-with-message");
    static final URI CONSTRAINT_VIOLATION_TYPE = URI.create(PROBLEM_BASE_URL + "/constraint-violation");
    public static final URI PARAMETERIZED_TYPE = URI.create(PROBLEM_BASE_URL + "/parameterized");
    static final URI INVALID_PASSWORD_TYPE = URI.create(PROBLEM_BASE_URL + "/invalid-password");
    static final URI EMAIL_EXISTED_TYPE = URI.create(PROBLEM_BASE_URL + "/email-already-used");
    static final URI LOGIN_EXISTED_TYPE = URI.create(PROBLEM_BASE_URL + "/login-existed");
    static final URI LOGIN_NOT_FOUND_TYPE = URI.create(PROBLEM_BASE_URL + "/login-not-found");
    static final URI MOBILE_EXISTED_TYPE = URI.create(PROBLEM_BASE_URL + "/mobile-already-used");
    static final URI MOBILE_NOT_FOUND_TYPE = URI.create(PROBLEM_BASE_URL + "/mobile-not-found");
    static final URI AUTHORITY_NOT_FOUND_TYPE = URI.create(PROBLEM_BASE_URL + "/authority-not-found");
    static final URI RESET_KEY_NOT_MATCH_TYPE = URI.create(PROBLEM_BASE_URL + "/reset-key-not-match");
    static final URI OUTGOING_BAD_REQUEST_TYPE = URI.create(PROBLEM_BASE_URL + "/outgoing_bad_request");
    static final URI RESOURCE_NOT_FOUND_TYPE = URI.create(PROBLEM_BASE_URL + "resource-not-found");
    static final URI EMAIL_NOT_FOUND_TYPE = URI.create(PROBLEM_BASE_URL + "/email-not-found");
    static final URI JDSMART_BAD_REQUEST_TYPE = URI.create(PROBLEM_BASE_URL + "/jdsmart-bad-request");

    private ErrorConstants() {
    }
}
