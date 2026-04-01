package com.example.BookSocialNetwork.controller;

import com.example.BookSocialNetwork.model.BookRequest;
import com.example.BookSocialNetwork.model.BookResponse;
import com.example.BookSocialNetwork.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("books")
@RequiredArgsConstructor
public class BookController {
    private final BookService service;

    @PostMapping("/")
    public ResponseEntity<Integer> saveBook(@Valid @RequestBody BookRequest request, Authentication connectedUser){
        return ResponseEntity.ok(service.save(request,connectedUser));
    }

    @GetMapping("/{book-id}")
    public ResponseEntity<BookResponse> findBookById(@Valid @PathVariable("book-id") Integer bookId){
        return ResponseEntity.ok(service.findById(bookId));
    }

}
