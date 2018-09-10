package com.twigcodes.uaa.repository;

import com.twigcodes.commons.problem.ResourceNotFoundException;
import com.twigcodes.uaa.domain.TwigClientRegistration;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

@RequiredArgsConstructor
public class TwigJdbcClientRegistrationRepository implements ClientRegistrationRepository {
  private final TwigClientRegistrationRepository twigClientRegistrationRepository;

  @Override
  public ClientRegistration findByRegistrationId(String registrationId) {
    return twigClientRegistrationRepository.findOneByRegistrationId(registrationId)
      .map(TwigClientRegistration::toClientRegistration)
      .orElseThrow(() -> new ResourceNotFoundException("未找到客户端注册信息"));
  }
}
