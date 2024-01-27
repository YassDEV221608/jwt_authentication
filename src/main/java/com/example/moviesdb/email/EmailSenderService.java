package com.example.moviesdb.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

@Service
public class EmailSenderService {
    @Autowired
    private JavaMailSender javaMailSender;
    public void sendVerificationEmail(String userEmail, String verificationToken) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(userEmail);
        message.setSubject("Email Verification");
        message.setText("Click the following link to verify your email: "
                + "http:localhost:8080/verify/" + verificationToken);
        try {
            javaMailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed the verification email");
        }
    }
}
