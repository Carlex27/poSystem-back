package com.softeams.poSystem.security.repositories;


import com.softeams.poSystem.security.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    List<User> findAll();
    List<User> findByRoles(String roles);
    List<User> findByUsernameContainingIgnoreCase(String username);
    boolean existsByUsername(String username);
    void deleteByUsername(String username);
}
