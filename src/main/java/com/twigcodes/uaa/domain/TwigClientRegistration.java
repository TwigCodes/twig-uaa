package com.twigcodes.uaa.domain;

import com.twigcodes.uaa.domain.converter.StringSetConverter;
import lombok.Data;

import javax.persistence.*;
import java.util.Collections;
import java.util.Set;

@Data
@Entity
@Table(name = "twig_client_registration")
public class TwigClientRegistration {
    @Id
    @Column(name = "registration_id")
    private String registrationId;
    @Column(name = "client_id")
    private String clientId;
    @Column(name = "client_secret")
    private String clientSecret;
    @Column(name = "client_auth_method")
    private String clientAuthenticationMethod = "basic";
    @Column(name = "auth_grant_type")
    private String authorizationGrantType = "authorization_code";
    @Column(name = "redirect_uri")
    private String redirectUriTemplate;
    @Column(name = "scopes")
    @Convert(converter = StringSetConverter.class)
    private Set<String> scopes = Collections.emptySet();
    @Column(name = "authorization_uri")
    private String authorizationUri;
    @Column(name = "token_uri")
    private String tokenUri;
    @Column(name = "user_info_uri")
    private String userInfoUri;
    @Column(name = "user_info_attribute_name")
    private String userNameAttributeName;
    @Column(name = "jwk_set_uri")
    private String jwkSetUri;
    @Column(name = "client_name")
    private String clientName;
}
