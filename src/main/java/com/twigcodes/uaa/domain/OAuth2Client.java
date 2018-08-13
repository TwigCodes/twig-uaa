package com.twigcodes.uaa.domain;

import com.twigcodes.uaa.domain.converter.StringSetConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "oauth_client_details")
public class OAuth2Client implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "client_id")
    private String clientId;
    @Column(name = "client_secret")
    private String clientSecret;
    @Column(name = "resource_ids")
    @Convert(converter = StringSetConverter.class)
    private Set<String> resourceIds;
    private String scope;
    @Column(name = "authorized_grant_types")
    @Convert(converter = StringSetConverter.class)
    private Set<String> authorizedGrantTypes;
    @Column(name = "web_server_redirect_uri")
    @Convert(converter = StringSetConverter.class)
    private Set<String> registeredRedirectUri;
    @Convert(converter = StringSetConverter.class)
    private Set<String> authorities;
    @Column(name = "access_token_validity")
    private Long accessTokenValidity;
    @Column(name = "refresh_token_validity")
    private Long refreshTokenValidity;
    @Column(name = "additional_information")
    private String additionalInformation;
}
