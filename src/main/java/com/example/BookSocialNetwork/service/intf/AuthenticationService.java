package com.example.BookSocialNetwork.service.intf;

import com.example.BookSocialNetwork.model.RegistrationRequest;
import jakarta.mail.MessagingException;

public interface AuthenticationService {
    void register(RegistrationRequest request) throws MessagingException;
}
