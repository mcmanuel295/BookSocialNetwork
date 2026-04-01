package com.example.BookSocialNetwork.model;

import com.example.BookSocialNetwork.entities.Books;

public class BookMapper {

    public Books toBook(BookRequest request) {
        return Books.builder()
                .id(request.id())
                .title(request.title())
                .authorName(request.authorName())
                .isbn(request.isbn())
                .synopsis(request.synopsis())
                .shareable(request.shareable() )
                .build();
    }
}
