package SalesianosLosBoscos.Backend.fashionBlogApi.controllers;

import SalesianosLosBoscos.Backend.fashionBlogApi.dto.User;
import SalesianosLosBoscos.Backend.fashionBlogApi.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("login")
    public ResponseEntity<User> loginUser(@RequestBody User user) {
        System.out.println("Attempting to log in user with email: " + user.getCorreo());
        Optional<User> existingUser = userRepository.findByCorreo(user.getCorreo());
        if (existingUser.isPresent() && existingUser.get().getPassword().equals(user.getPassword())) {
            System.out.println("Login successful for user: " + user.getCorreo());
            return ResponseEntity.ok(existingUser.get());
        }
        System.out.println("Login failed for user: " + user.getCorreo());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody User user) {
        if (userRepository.findByCorreo(user.getCorreo()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        User savedUser = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @GetMapping("users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        if (!users.isEmpty()) {
            return ResponseEntity.ok(users);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}
