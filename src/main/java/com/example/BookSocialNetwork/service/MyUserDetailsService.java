package com.example.BookSocialNetwork.service;

import com.example.BookSocialNetwork.entities.User;
import com.example.BookSocialNetwork.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {
    private final UserRepository userRepo;


    @Transactional
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
                User user =userRepo
                .findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User Not Found"));

                return new MyUserDetails(user);
    }
}
