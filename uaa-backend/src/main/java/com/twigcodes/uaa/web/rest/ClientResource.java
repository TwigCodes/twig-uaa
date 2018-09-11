package com.twigcodes.uaa.web.rest;

import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/clients")
public class ClientResource {
  private final JdbcClientDetailsService clientsDetailsService;

  @PostMapping("/")
  public void addClient(@RequestBody ClientDetails clientDetails) {
    clientsDetailsService.addClientDetails(clientDetails);
  }

  @PutMapping("{client.clientId}")
  public void updateClient(@RequestBody ClientDetails clientDetails, @PathVariable("client.clientId") String id) {
    clientsDetailsService.updateClientDetails(clientDetails);
  }
  @DeleteMapping("{client.clientId}/delete")
  public void deleteClient(@RequestBody BaseClientDetails ClientDetails, @PathVariable("client.clientId") String id) {
    clientsDetailsService.removeClientDetails(clientsDetailsService.loadClientByClientId(id).toString());
  }
}
