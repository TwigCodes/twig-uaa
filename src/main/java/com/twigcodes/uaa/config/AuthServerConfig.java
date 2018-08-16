package com.twigcodes.uaa.config;

import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import com.twigcodes.uaa.config.token.TenantAwareTokenEnhancer;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.DefaultUserInfoRestTemplateFactory;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoRestTemplateCustomizer;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoRestTemplateFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.util.Assert;

import lombok.RequiredArgsConstructor;
import lombok.val;

/**
 * AuthServerConfig
 */
@EnableAuthorizationServer
@Configuration
@RequiredArgsConstructor
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

    @Value("${uaa.jwtSigningKey}")
    private String jwtSigningKey;
    private final DataSource dataSource;
    private final PasswordEncoder oauthPasswordEncoder;
    private final AuthenticationManager authenticationManager;


    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
        oauthServer
            .tokenKeyAccess("isAuthenticated()")
            .checkTokenAccess("permitAll()")
            .passwordEncoder(oauthPasswordEncoder);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 配置客户端信息，从数据库中读取，对应 oauth_client_details 表
        clients.withClientDetails(jdbcClientDetailsService());
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // 配置 token 的数据源、自定义的 tokenServices 等信息
        // 配置身份认证器，配置认证方式，TokenStore，TokenGranter，OAuth2RequestFactory
        endpoints
            .tokenStore(tokenStore())
            .tokenEnhancer(tokenEnhancerChain())
            .approvalStore(approvalStore())
            .authorizationCodeServices(authorizationCodeServices())
            .authenticationManager(authenticationManager);
    }

    @Bean
    public ApprovalStore approvalStore() {
        return new JdbcApprovalStore(dataSource);
    }

    @Bean
    protected AuthorizationCodeServices authorizationCodeServices() {
        // 授权码存储等处理方式类，使用 jdbc，操作 oauth_code 表
        return new JdbcAuthorizationCodeServices(dataSource);
    }

    @Bean
    public JdbcClientDetailsService jdbcClientDetailsService() {
       val clientDetailsService = new JdbcClientDetailsService(dataSource);
       clientDetailsService.setPasswordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder());
       return clientDetailsService;
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(this.accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        Assert.notNull(jwtSigningKey, "No JWT signing key present, check config params for application.jwtSigningKey");

        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(jwtSigningKey);
        return converter;
    }

    @Bean
    public TokenEnhancerChain tokenEnhancerChain() {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(new TenantAwareTokenEnhancer(), accessTokenConverter()));
        return tokenEnhancerChain;
    }

    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setSupportRefreshToken(true);
        return defaultTokenServices;
    }

    @Bean
    public UserInfoRestTemplateFactory userInfoRestTemplateFactory(
        ObjectProvider<List<UserInfoRestTemplateCustomizer>> customizers,
        ObjectProvider<OAuth2ProtectedResourceDetails> details,
        ObjectProvider<OAuth2ClientContext> context) {
        return new DefaultUserInfoRestTemplateFactory(customizers, details, context);
    }


}
