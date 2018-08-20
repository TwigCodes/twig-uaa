package com.twigcodes.uaa.config;

import com.twigcodes.uaa.config.security.PersistOAuth2UserService;
import com.twigcodes.uaa.domain.Authority;
import com.twigcodes.uaa.repository.TwigClientRegistrationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.endpoint.NimbusAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@EnableOAuth2Client
@Order(200)
@Configuration
public class ServerSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final TwigClientRegistrationRepository twigClientRegistrationRepository;

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
      throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable()
            .and()
                .csrf()
                .ignoringAntMatchers("/actuator/**", "/h2-console/**")
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            .and()
                .authorizeRequests()
                .antMatchers("/h2-console/**", "/login/oauth2/code/**", "/login**", "/webjars/**", "/actuator/**", "/v2/api-docs", "/swagger**").permitAll()
                .anyRequest().authenticated()
            .and()
                .formLogin().loginProcessingUrl("/login.do")
                .usernameParameter("username")
                .passwordParameter("password")
                .loginPage("/login")
                .permitAll()
            .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout.do"));
            http
                .oauth2Login()
                .loginPage("/login")
                .clientRegistrationRepository(clientRegistrationRepository())
                .authorizedClientService(authorizedClientService())
                .authorizationEndpoint()
                    .baseUri("/login/oauth2/authorization")
                    .authorizationRequestRepository(authorizationRequestRepository())
            .and()
                .tokenEndpoint()
                    .accessTokenResponseClient(accessTokenResponseClient())
            .and()
                .userInfoEndpoint()
                    .userAuthoritiesMapper(this.userAuthoritiesMapper())
                    .userService(persistOAuth2UserService())
//            .and()
//                .redirectionEndpoint()
//                    .baseUri("/login/oauth2/code")
            ;
    }

    private GrantedAuthoritiesMapper userAuthoritiesMapper() {
        return (authorities) -> {
            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
            mappedAuthorities.add(Authority.builder().authority("ROLE_ADMIN").build());
//            authorities.forEach(authority -> {
//                OAuth2UserAuthority oauth2UserAuthority = (OAuth2UserAuthority)authority;
//                Map<String, Object> userAttributes = oauth2UserAuthority.getAttributes();
//
//            });

            return mappedAuthorities;
        };
    }

    @Bean
    public OAuth2AuthorizedClientService authorizedClientService() {
        return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository());
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        val clientRegistrations = twigClientRegistrationRepository.findAll().stream().map(twigClientRegistration ->
            ClientRegistration.withRegistrationId(twigClientRegistration.getRegistrationId())
                .clientId(twigClientRegistration.getClientId())
                .clientSecret(twigClientRegistration.getClientSecret())
                .authorizationGrantType(new AuthorizationGrantType(twigClientRegistration.getAuthorizationGrantType()))
                .clientAuthenticationMethod(new ClientAuthenticationMethod(twigClientRegistration.getClientAuthenticationMethod()))
                .authorizationUri(twigClientRegistration.getAuthorizationUri())
                .tokenUri(twigClientRegistration.getTokenUri())
                .jwkSetUri(twigClientRegistration.getJwkSetUri())
                .userInfoUri(twigClientRegistration.getUserInfoUri())
                .clientName(twigClientRegistration.getClientName())
                .userNameAttributeName(twigClientRegistration.getUserNameAttributeName())
                .scope(twigClientRegistration.getScopes().toArray(new String[0]))
                .redirectUriTemplate(twigClientRegistration.getRedirectUriTemplate())
                .build()
        ).collect(Collectors.toList());
        return new InMemoryClientRegistrationRepository(clientRegistrations);
    }

    @Bean
    public AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository() {
        return new HttpSessionOAuth2AuthorizationRequestRepository();
    }

    @Bean
    public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient() {
        return new NimbusAuthorizationCodeTokenResponseClient();
    }

    @Primary
    @Bean
    public PersistOAuth2UserService persistOAuth2UserService() {
        return new PersistOAuth2UserService();
    }

}
