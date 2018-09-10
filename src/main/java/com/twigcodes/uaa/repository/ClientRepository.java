package com.twigcodes.uaa.repository;

import com.twigcodes.uaa.domain.OAuth2Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<OAuth2Client, String> {}
