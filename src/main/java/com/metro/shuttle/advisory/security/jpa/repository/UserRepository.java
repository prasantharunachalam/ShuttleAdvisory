package com.metro.shuttle.advisory.security.jpa.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.metro.shuttle.advisory.security.entity.User;

/**
 * User repository for CRUD operations.
 */
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);
}
