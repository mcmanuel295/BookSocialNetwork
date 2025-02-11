package com.example.BookSocialNetwork.service;

import com.example.BookSocialNetwork.model.AuthenticationRequest;
import com.example.BookSocialNetwork.model.AuthenticationResponse;
import com.example.BookSocialNetwork.model.EmailTemplateName;
import com.example.BookSocialNetwork.model.RegistrationRequest;
import com.example.BookSocialNetwork.entities.Token;
import com.example.BookSocialNetwork.entities.User;
import com.example.BookSocialNetwork.repository.RoleRepository;
import com.example.BookSocialNetwork.repository.TokenRepository;
import com.example.BookSocialNetwork.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService{
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepo;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public void register(RegistrationRequest request) throws MessagingException {
        var userRole =roleRepo.findByName("USER")
                .orElseThrow(()-> new IllegalStateException("ROLE USER was not initialized"));

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

    private void sendValidationEmail(User user) throws MessagingException {
        var newToken = generateAndSaveActivationToken(user);
        String activationUrl = "http://localhost:4200/activate-account";
        emailService.sendEmail(
                user.getEmail(),
                user.fullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "Account activation"
        );
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

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword())
        );
        var claims = new HashMap<String,Object>();
        var user = (User)auth.getPrincipal();
        claims.put("fullName",user.fullName());
        var jwtToken = jwtService.generateToken(claims,new MyUserDetails(user));
        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .build();

     }

    @Transactional
    public void activateAccount(String token) throws MessagingException {
        Token savedToken = tokenRepository.findByToken(token)
                .orElseThrow(()->
                new RuntimeException("Invalid token"));
        if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
            sendValidationEmail(savedToken.getUser());
            throw new RuntimeException("Activation token has expired. A new token has been sent to same email address ");
        }

        var user = userRepo
                .findById(savedToken.getUser()
                        .getUserId()).orElseThrow(()->
                        new RuntimeException("user not found"));
        user.setEnabled(true);
        userRepo.save(user);
        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);

    }
}
