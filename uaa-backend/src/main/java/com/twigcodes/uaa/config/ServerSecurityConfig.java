package com.twigcodes.uaa.config;

import com.twigcodes.uaa.config.security.PersistOAuth2UserService;
import com.twigcodes.uaa.domain.Authority;
import com.twigcodes.uaa.repository.TwigClientRegistrationRepository;
import com.twigcodes.uaa.repository.TwigJdbcClientRegistrationRepository;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.endpoint.NimbusAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Slf4j
@RequiredArgsConstructor
@EnableOAuth2Client
@Order(200)
@Configuration
public class ServerSecurityConfig extends WebSecurityConfigurerAdapter {
  private final TwigClientRegistrationRepository twigClientRegistrationRepository;
  private final UserDetailsService userDetailsService;

  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring()
      .antMatchers(
        "/static/**",
        "/*.js",
        "/*.css",
        "/webjars/**",
        "/favicon.ico",
        "*.bundle.*");
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
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
          .ignoringAntMatchers(
            "/actuator/**",
            "/h2-console/**",
            "/swagger-ui.html**")
          .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
      .and()
        .authorizeRequests()
          .antMatchers(
              HttpMethod.GET,
              "/",
              "/login",
              "/index.html",
              "/v2/api-docs",
              "/actuator/**",
              "/h2-console/**",
              "/swagger-ui.html**",
              "/swagger-resources/**").permitAll()
          .anyRequest().authenticated()
      .and()
        .logout().logoutSuccessUrl("/");
    http.oauth2Login()
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
      ;
  }

  private GrantedAuthoritiesMapper userAuthoritiesMapper() {
    return (authorities) -> {
      Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
      authorities.forEach(
          authority -> {
            OAuth2UserAuthority oauth2UserAuthority = (OAuth2UserAuthority) authority;
            mappedAuthorities.add(
                Authority.builder().authority(oauth2UserAuthority.getAuthority()).build());
          });
      return mappedAuthorities;
    };
  }

  @Bean
  public OAuth2AuthorizedClientService authorizedClientService() {
    return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository());
  }

  @Bean
  public ClientRegistrationRepository clientRegistrationRepository() {
      return new TwigJdbcClientRegistrationRepository(twigClientRegistrationRepository);
  }

  @Bean
  public AuthorizationRequestRepository<OAuth2AuthorizationRequest>
      authorizationRequestRepository() {
    return new HttpSessionOAuth2AuthorizationRequestRepository();
  }

  @Bean
  public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest>
      accessTokenResponseClient() {
    return new NimbusAuthorizationCodeTokenResponseClient();
  }

  @Primary
  @Bean
  public PersistOAuth2UserService persistOAuth2UserService() {
    return new PersistOAuth2UserService();
  }
}
