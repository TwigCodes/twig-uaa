package com.twigcodes.uaa.repository;

import com.twigcodes.uaa.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
  Optional<User> findOneByUsername(String username);
}
