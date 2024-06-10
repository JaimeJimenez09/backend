package SalesianosLosBoscos.Backend.fashionBlogApi.controllers;

import SalesianosLosBoscos.Backend.fashionBlogApi.dto.User;
import SalesianosLosBoscos.Backend.fashionBlogApi.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/user")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = userRepository.findAll();
        if (!users.isEmpty()) {
            return ResponseEntity.ok(users);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Integer id){
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/")
    public ResponseEntity<User> addUser(@Valid @RequestBody User user){
        User savedUser = userRepository.save(user);
        if (savedUser.getUserId() != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Integer id){
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            userRepository.delete(user);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Integer id, @Valid @RequestBody User userDetails) {
        return userRepository.findById(id).map(user -> {
            user.setName(userDetails.getName());
            user.setCorreo(userDetails.getCorreo());
            user.setPassword(userDetails.getPassword());
            userRepository.save(user);
            return ResponseEntity.ok(user);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<User> editUser(@PathVariable Integer id, @Valid @RequestBody User user) {
        System.out.println("Request received for user ID: " + id);

        if (user == null || user.getUserId() == null) {
            System.out.println("Bad request: User or user ID is null");
            return ResponseEntity.badRequest().build();
        }

        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser == null) {
            System.out.println("User not found: ID " + id);
            return ResponseEntity.notFound().build();
        }

        System.out.println("Updating user: " + existingUser.getUserId());

        if (user.getName() != null) existingUser.setName(user.getName());
        if (user.getCorreo() != null) existingUser.setCorreo(user.getCorreo());
        if (user.getPassword() != null && user.getPassword().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")) {
            existingUser.setPassword(user.getPassword());
        }

        userRepository.save(existingUser);
        System.out.println("User updated successfully: " + existingUser.getUserId());

        return ResponseEntity.ok(existingUser);
    }
}
