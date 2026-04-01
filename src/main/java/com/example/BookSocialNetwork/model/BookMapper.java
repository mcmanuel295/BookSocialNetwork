package com.example.BookSocialNetwork.model;

import com.example.BookSocialNetwork.book.BorrowedBookResponse;
import com.example.BookSocialNetwork.entities.BookTransactionHistory;
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

    public BookResponse toBookResponse(Books books) {
        return BookResponse.builder()
                .id(books.getId())
                .title(books.getTitle())
                .authorName(books.getAuthorName())
                .isbn(books.getIsbn())
                .synopsis(books.getSynopsis())
                .shareable(books.isShareable() )
                .build();
    }

    public BorrowedBookResponse toBorrowedBookResponse(BookTransactionHistory history){
         return BorrowedBookResponse.builder()
                 .id(history.getId())
                 .title(history.getBook().getTitle())
                 .authorName(history.getBook().getAuthorName())
                 .isbn(history.getBook().getIsbn())
                 .rate(history.getBook().getRate())
                 .returned(history.isReturned())
                 .returnedApproved(history.isReturnApproved())
                 .build();
}
}
