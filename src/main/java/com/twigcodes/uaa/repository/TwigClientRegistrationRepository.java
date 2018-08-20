package com.twigcodes.uaa.repository;

import com.twigcodes.uaa.domain.TwigClientRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TwigClientRegistrationRepository extends JpaRepository<TwigClientRegistration, String> {
    TwigClientRegistration findByRegistrationId(String registrationId);
}
