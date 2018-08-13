package com.twigcodes.uaa.service;

import com.twigcodes.uaa.domain.OAuth2Client;
import com.twigcodes.uaa.repository.ClientRepository;
import com.twigcodes.uaa.service.dto.ClientDTO;
import com.twigcodes.uaa.web.rest.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ClientService {
    private final ClientRepository clientRepository;

    public ClientDTO add(ClientDTO clientDTO) {
        val client = clientDTO.toClient();
        val saved = clientRepository.save(client);
        return new ClientDTO(saved);
    }

    public void delete(String id) {
        clientRepository.deleteById(id);
    }

    public ClientDTO update(String id, ClientDTO clientDTO) {
        return clientRepository.findById(id).map(client -> {
            val toSave = OAuth2Client.builder()
                .clientSecret(clientDTO.getClientSecret())
                .scope(clientDTO.getScope())
                .authorities(clientDTO.getAuthorities())
                .authorizedGrantTypes(clientDTO.getAuthorizedGrantTypes())
                .resourceIds(clientDTO.getResourceIds())
                .registeredRedirectUri(clientDTO.getRegisteredRedirectUri())
                .accessTokenValidity(clientDTO.getAccessTokenValidity())
                .refreshTokenValidity(clientDTO.getRefreshTokenValidity())
                .build();
            val saved = clientRepository.save(toSave);
            return new ClientDTO(saved);
        }).orElseThrow(() -> new ResourceNotFoundException("未找到 clientId 为 " + id + " 的 Client"));
    }

    public Page<ClientDTO> findAll(Pageable pageable) {
        return clientRepository.findAll(pageable).map(ClientDTO::new);
    }
}
