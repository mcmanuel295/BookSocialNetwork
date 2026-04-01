package com.example.BookSocialNetwork.service;

import com.example.BookSocialNetwork.book.BorrowedBookResponse;
import com.example.BookSocialNetwork.entities.BookTransactionHistory;
import com.example.BookSocialNetwork.entities.Books;
import com.example.BookSocialNetwork.entities.User;
import com.example.BookSocialNetwork.model.*;
import com.example.BookSocialNetwork.repository.BookRepository;
import com.example.BookSocialNetwork.repository.BookTransactionHistoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;


@RequiredArgsConstructor
@Service
public class BookService {
    private final BookMapper bookMapper;
    private final BookRepository bookRepo;
    private final BookTransactionHistoryRepository historyRepository;

    public Integer save(BookRequest request, Authentication connectedUser) {
        return null;
    }


    public BookResponse findById(Integer bookId) {
        return bookRepo.findById(bookId)
                .map(bookMapper::toBookResponse)
        .orElseThrow(()-> new EntityNotFoundException("No book found with Id::"+bookId));
    }

    public PageResponse<BookResponse> findAllBooks(int pageNo, int size, Authentication connectedUser) {
        User user = (User) connectedUser;
        Pageable pageable = PageRequest.of(pageNo,size, Sort.by("createdDate").ascending());
        Page<Books> books= bookRepo.findAllDisplayableBooks(pageable,user.getUserId());

        List<BookResponse> bookResponse = books.stream().map(bookMapper::toBookResponse).toList();
        return new PageResponse<>(
                bookResponse,
                books.getNumber()
                ,books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }

    public PageResponse<BookResponse> findAllBooksByOwner(int pageNo, int size, Authentication connectedUser) {
        User user = (User) connectedUser;
        Pageable pageable = PageRequest.of(pageNo,size, Sort.by("createdDate").ascending());
        Page<Books> books= bookRepo.findAll(BookSpecification.withOwner(user.getUserId()),pageable);
        List<BookResponse> bookResponse = books.stream().map(bookMapper::toBookResponse).toList();
        return new PageResponse<>(
                bookResponse,
                books.getNumber()
                ,books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );

    }

    public PageResponse<BookResponse> findAllBorrowedBooks(int pageNo, int size, Authentication connectedUser) {
        User user = (User) connectedUser;
        Pageable pageable = PageRequest.of(pageNo,size, Sort.by("createdDate").ascending());
        Page<BookTransactionHistory> allBorrowedBooks = historyRepository.findAllBorrowedBooks(pageable,user.getUserId());
        List<BorrowedBookResponse> bookResponse = allBorrowedBooks.stream()
                .map(bookMapper:: toBorrowedBookResponse)
                .toList();
    }
}
