package com.twigcodes.uaa.config;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.context.request.RequestContextListener;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

import lombok.RequiredArgsConstructor;

@Import(SecurityProblemSupport.class)
@RequiredArgsConstructor
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private final SecurityProblemSupport problemSupport;
    private final AppProperties appProperties;

    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId("uaa");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.exceptionHandling()
            .authenticationEntryPoint(problemSupport)
            .accessDeniedHandler(problemSupport)
        .and()
            .requestMatcher(new OAuthRequestedMatcher(appProperties.getSecurity()))
            .authorizeRequests()
            .anyRequest().fullyAuthenticated();
    }

    @RequiredArgsConstructor
    private static class OAuthRequestedMatcher implements RequestMatcher {
        private final AppProperties.Security security;
        public boolean matches(HttpServletRequest request) {
            String auth = request.getHeader(security.getAuthorization().getHeader());
            boolean haveOauth2Token = (auth != null) && auth.startsWith(security.getJwt().getTokenPrefix());
            boolean haveAccessToken = request.getParameter("access_token")!=null;
            return haveOauth2Token || haveAccessToken;
        }
    }
}
