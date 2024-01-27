package com.example.moviesdb.user.auth;

import com.example.moviesdb.email.EmailSenderService;
import com.example.moviesdb.user.Role;
import com.example.moviesdb.user.User;
import com.example.moviesdb.user.UserRepository;
import com.example.moviesdb.user.config.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class AuthenticationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private EmailSenderService emailSenderService;

    public AuthenticationService(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    public AuthenticationResponse register(RegisterRequest request) {
        User user = new User(request.getName(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                Role.USER);
        String token = jwtService.generateToken(new HashMap<>(),user);
        user.setToken(token);
        user.setVerified(false);
        emailSenderService.sendVerificationEmail(user.getEmail(),token);
        userRepository.save(user);
        return new AuthenticationResponse(user.getToken());
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        ));
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));
        if(!user.isVerified()) {
            throw new RuntimeException("invalid user");
        }
        String token = user.getToken();
        return new AuthenticationResponse(token);
    }
}
