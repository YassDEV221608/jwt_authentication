package com.example.moviesdb.email;


import com.example.moviesdb.user.User;
import com.example.moviesdb.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@CrossOrigin("http://localhost:3000/")
public class EmailController {
    @Autowired
    private UserRepository userRepository;


    @GetMapping("/verify/{token}")
    public ResponseEntity<String> verifyEmail(@PathVariable("token") String verificationToken) {
        User user = userRepository.findByToken(verificationToken);
        if(user != null) {
            user.setVerified(true);
            userRepository.save(user);
            return ResponseEntity.ok().body("{\"message\": \"Your email address has been verified\"}");
        }
        return ResponseEntity.status(404).body("{\"error\": \"User not found or verification token is invalid\"}");
    }
}
