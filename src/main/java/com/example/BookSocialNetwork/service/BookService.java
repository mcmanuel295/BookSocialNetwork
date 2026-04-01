package com.example.BookSocialNetwork.service;

import com.example.BookSocialNetwork.entities.Books;
import com.example.BookSocialNetwork.entities.User;
import com.example.BookSocialNetwork.model.BookMapper;
import com.example.BookSocialNetwork.model.BookRequest;
import com.example.BookSocialNetwork.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookService {
    private final BookMapper bookMapper;
    private final BookRepository bookRepo;

    public Integer save(BookRequest request, Authentication connectedUser) {
        return null;
    }


    public BookResponse findById(Integer bookId) {
    }
}
