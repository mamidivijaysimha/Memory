package com.example.memorylane.controller;

import com.example.memorylane.model.User;
import com.example.memorylane.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "https://memoryfrontend-o473.onrender.com", allowCredentials = "true")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        Optional<User> existing = userRepository.findByUsername(user.getUsername());
        if (existing.isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        User newUser = new User(user.getUsername(), user.getPassword());
        userRepository.save(newUser);
        return ResponseEntity.ok(newUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        Optional<User> existing = userRepository.findByUsername(user.getUsername());
        if (existing.isPresent() && existing.get().getPassword().equals(user.getPassword())) {
            Map<String, String> response = new HashMap<>();
            response.put("username", existing.get().getUsername());
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }

    @GetMapping("/me")
    public ResponseEntity<?> getLoggedInUser(@RequestParam String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.status(404).body("User not found");
        }
    }

    // ✅ NEW: Update username and password
    @PutMapping("/update")
    public ResponseEntity<?> updateCredentials(@RequestBody Map<String, String> body) {
        String oldUsername = body.get("oldUsername");
        String oldPassword = body.get("oldPassword");
        String newUsername = body.get("newUsername");
        String newPassword = body.get("newPassword");

        Optional<User> optionalUser = userRepository.findByUsernameAndPassword(oldUsername, oldPassword);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(401).body("Old username or password incorrect");
        }

        User user = optionalUser.get();
        user.setUsername(newUsername);
        user.setPassword(newPassword);
        userRepository.save(user);

        return ResponseEntity.ok("User updated successfully");
    }
}
