package com.example.BookSocialNetwork.service.impl;

import com.example.BookSocialNetwork.model.RegistrationRequest;
import com.example.BookSocialNetwork.entities.Token;
import com.example.BookSocialNetwork.entities.User;
import com.example.BookSocialNetwork.repository.RoleRepository;
import com.example.BookSocialNetwork.repository.TokenRepository;
import com.example.BookSocialNetwork.repository.UserRepository;
import com.example.BookSocialNetwork.service.EmailService;
import com.example.BookSocialNetwork.service.intf.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImp implements AuthenticationService {
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepo;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;

    @Override
    public void register(RegistrationRequest request) {
        var userRole =roleRepo.findByName("USER")
//                todo :better exception handling
                .orElseThrow(()->
                new IllegalStateException("ROLE USER was not initialized"));

        var user = User
                .builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .enabled(false)
                .roles(List.of(userRole))
                .build();
        userRepo.save(user);
        sendValidationEmail(user);
    }

    private void sendValidationEmail(User user) {
        var newToken = generateAndSaveActivationToken(user);

//        todo : send email

        
    }

    private String generateAndSaveActivationToken(User user) {
        String generatedToken = generateActivationCode(6);

        var token = Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        tokenRepository.save(token);
        return generatedToken;
    }

    private String generateActivationCode(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();

        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append((characters.charAt(randomIndex)));
        }
            return codeBuilder.toString();
    }
}
