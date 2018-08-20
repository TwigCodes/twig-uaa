package com.twigcodes.uaa.config.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.token.ClientTokenServices;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OAuth2ClientAuthenticationProcessingAndSavingFilter extends OAuth2ClientAuthenticationProcessingFilter {
    private ClientTokenServices clientTokenServices;

    public OAuth2ClientAuthenticationProcessingAndSavingFilter(String defaultFilterProcessesUrl, ClientTokenServices clientTokenServices) {
        super(defaultFilterProcessesUrl);
        this.clientTokenServices = clientTokenServices;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        if (clientTokenServices != null) {
            clientTokenServices.saveAccessToken(restTemplate.getResource(), SecurityContextHolder.getContext()
                .getAuthentication(), restTemplate.getAccessToken());
        }
    }

}
