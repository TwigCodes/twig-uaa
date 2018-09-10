package com.twigcodes.uaa.repository;

import com.twigcodes.uaa.domain.TwigClientRegistration;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TwigClientRegistrationRepository
    extends JpaRepository<TwigClientRegistration, String> {
  Optional<TwigClientRegistration> findOneByRegistrationId(String registrationId);
}
