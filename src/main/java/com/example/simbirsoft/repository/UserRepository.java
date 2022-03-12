package com.example.simbirsoft.repository;

import com.example.simbirsoft.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("""
              SELECT user
              FROM User user
              WHERE user.email = :email""")
    Optional<User> findByEmail(String email);
}
