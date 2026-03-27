package com.example.scaler_capstone_project.repositories;

import com.example.scaler_capstone_project.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Override
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
}
