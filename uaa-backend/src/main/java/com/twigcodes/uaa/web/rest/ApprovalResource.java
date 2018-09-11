package com.twigcodes.uaa.web.rest;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.approval.Approval;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class ApprovalResource {
  private final JdbcClientDetailsService clientDetailsService;
  private final ApprovalStore approvalStore;
  private final TokenStore tokenStore;

  // @GetMapping("/approvals")
  // public List<Approval> getApprovals() {
  //   return clientDetailsService
  //     .listClientDetails()
  //     .stream()
  //     .map(clientDetails -> approvalStore.getApprovals(principal.getName(), clientDetails.getClientId()))
  //     .flatMap(Collection::stream)
  //     .collect(Collectors.toList());
  // }

  @PostMapping("/approval/revoke")
  public void revokApproval(@RequestBody Approval approval) {

    approvalStore.revokeApprovals(Collections.singletonList(approval));
    tokenStore
        .findTokensByClientIdAndUserName(approval.getClientId(), approval.getUserId())
        .forEach(tokenStore::removeAccessToken);
  }
}
