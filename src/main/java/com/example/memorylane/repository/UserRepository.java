package com.example.memorylane.repository;

import com.example.memorylane.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);

    // ✅ NEW: find by username and password
    Optional<User> findByUsernameAndPassword(String username, String password);
}
