package com.example.moviesdb.user;


import com.example.moviesdb.email.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin("http://localhost:3000/")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;


    @GetMapping("/getUser/{token}")
    public User getUserByToken(@PathVariable("token") String token) {
        User user = userRepository.findByToken(token);
        return user;
    }

    @PatchMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody User reqUser) {
        Optional<User> user = userRepository.findByEmail(reqUser.getEmail());
        if(user.isPresent()) {
            if(passwordEncoder.matches(reqUser.getPassword(),user.get().getPassword())) {
                return ResponseEntity.status(404).body("{\"message\": \"Password already exists\"}");
            }
            user.get().setPassword(passwordEncoder.encode(reqUser.getPassword()));
            user.get().setVerified(false);
            userRepository.save(user.get());
            emailSenderService.sendVerificationEmail(user.get().getEmail(),user.get().getToken());
            return ResponseEntity.ok("{\"message\": \"Successfully rested password\"}");
        }
        return ResponseEntity.status(404).body("{\"message\": \"User not found\"}");
    }


}
