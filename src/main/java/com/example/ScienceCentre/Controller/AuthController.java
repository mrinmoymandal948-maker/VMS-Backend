package com.example.ScienceCentre.Controller;

import com.example.ScienceCentre.DTO.RequestDto.LoginRequestDto;
import com.example.ScienceCentre.Model.User;
import com.example.ScienceCentre.Repository.CentreRepository;
import com.example.ScienceCentre.Repository.UserRepository;
import com.example.ScienceCentre.Security.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CentreRepository centreRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/create-user")
    public ResponseEntity<?> createUser(@RequestBody User newUser) {
        try {
            // 1. Check Admin Duplicate Rule
            if ("ADMIN".equalsIgnoreCase(newUser.getRole())) {
                boolean adminExists = userRepository.existsByRole("ADMIN");
                if (adminExists) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Admin already exists");
                }
            }

            // 2. Auto-assign centre for ALL roles if not provided in JSON
            if (newUser.getCentre() == null) {
                newUser.setCentre(
                        centreRepository.findAll()
                                .stream()
                                .findFirst()
                                .orElseThrow(() -> new RuntimeException("No centre found in database"))
                );
            }

            // 3. Encrypt Password
            newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

            User savedUser = userRepository.save(newUser);
            return ResponseEntity.ok(savedUser);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("DB Error: " + e.getMessage());
        }
    }

    // CHECK IF ADMIN EXISTS (for first-time setup)
    @GetMapping("/admin-exists")
    public ResponseEntity<?> adminExists() {
        boolean exists = userRepository.existsByRole("ADMIN");
        return ResponseEntity.ok(
                Map.of("exists", exists)
        );
    }

    //  GET ALL USERS
    @GetMapping("/all")
    @Transactional(readOnly = true)
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    //  LOGIN (ENCRYPTED PASSWORD MATCH)
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequestDto loginRequest) {

        User user = userRepository
                .findByUsernameAndActiveTrue(loginRequest.getUsername())
                .filter(u ->
                        passwordEncoder.matches(
                                loginRequest.getPassword(),
                                u.getPassword()
                        )
                )
                .orElse(null);

        if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");
        }

        if (user.getCentre() == null) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("User has no centre assigned");
        }

        String token = jwtService.generateToken(user.getUsername());

        Map<String, Object> response = Map.of(
                "token", token,
                "username", user.getUsername(),
                "role", user.getRole(),
                "centreId", user.getCentre().getId()
        );

        return ResponseEntity.ok(response);
    }

    // DELETE USER
    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            if (!userRepository.existsById(id)) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("Error: User not found");
            }
            userRepository.deleteById(id);
            return ResponseEntity.ok("User deleted successfully");

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("DB Error: " + e.getMessage());
        }
    }
}
