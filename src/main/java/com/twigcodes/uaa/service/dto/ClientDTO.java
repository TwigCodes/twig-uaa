package com.twigcodes.uaa.service.dto;

import com.twigcodes.uaa.domain.OAuth2Client;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ClientDTO {
    private String clientId;
    private String clientSecret;
    private Set<String> resourceIds;
    private String scope;
    private Set<String> authorizedGrantTypes;
    private Set<String> registeredRedirectUri;
    private Set<String> authorities;
    private Long accessTokenValidity;
    private Long refreshTokenValidity;
    private String additionalInformation;

    public ClientDTO(OAuth2Client client) {
        this.accessTokenValidity = client.getAccessTokenValidity();
        this.additionalInformation = client.getAdditionalInformation();
        this.authorities = client.getAuthorities();
        this.clientId = client.getClientId();
        this.clientSecret = client.getClientSecret();
        this.authorizedGrantTypes = client.getAuthorizedGrantTypes();
        this.refreshTokenValidity = client.getRefreshTokenValidity();
        this.registeredRedirectUri = client.getRegisteredRedirectUri();
        this.resourceIds = client.getResourceIds();
        this.scope = client.getScope();
    }

    public OAuth2Client toClient() {
        return OAuth2Client.builder()
            .accessTokenValidity(accessTokenValidity)
            .refreshTokenValidity(refreshTokenValidity)
            .registeredRedirectUri(registeredRedirectUri)
            .resourceIds(resourceIds)
            .authorizedGrantTypes(authorizedGrantTypes)
            .authorities(authorities)
            .scope(scope)
            .additionalInformation(additionalInformation)
            .clientId(clientId)
            .clientSecret(clientSecret)
            .build();
    }
}
