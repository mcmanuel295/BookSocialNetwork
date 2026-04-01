package com.example.BookSocialNetwork.service;

import com.example.BookSocialNetwork.book.BorrowedBookResponse;
import com.example.BookSocialNetwork.entities.BookTransactionHistory;
import com.example.BookSocialNetwork.entities.Books;
import com.example.BookSocialNetwork.entities.User;
import com.example.BookSocialNetwork.exception.OperationNotPermittedException;
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
import java.util.Objects;


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

    public PageResponse<BorrowedBookResponse> findAllBorrowedBooks(int pageNo, int size, Authentication connectedUser) {
        User user = (User) connectedUser;
        Pageable pageable = PageRequest.of(pageNo,size, Sort.by("createdDate").ascending());
        Page<BookTransactionHistory> allBorrowedBooks = historyRepository.findAllBorrowedBooks(pageable,user.getUserId());
        List<BorrowedBookResponse> bookResponse = allBorrowedBooks.stream()
                .map(bookMapper:: toBorrowedBookResponse)
                .toList();

        return new PageResponse<>(
                bookResponse,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );
    }

    public PageResponse<BorrowedBookResponse> findAllReturnedBooks(int pageNo, int size, Authentication connectedUser) {
        User user = (User) connectedUser;
        Pageable pageable = PageRequest.of(pageNo,size, Sort.by("createdDate").ascending());
        Page<BookTransactionHistory> allBorrowedBooks = historyRepository.findAllReturnedBooks(pageable,user.getUserId());
        List<BorrowedBookResponse> bookResponse = allBorrowedBooks.stream()
                .map(bookMapper:: toBorrowedBookResponse)
                .toList();

        return new PageResponse<>(
                bookResponse,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );
    }


    public Integer updateShareableStatus(Integer bookId, Authentication connectedUser) {
        Books book =bookRepo.findById(bookId).orElseThrow(() -> new EntityNotFoundException("No book found with the ID:: "+bookId));
        User user = (User) connectedUser;
        if (!Objects.equals(book.getOwner().getUserId(),user.getUserId() )){
            throw new OperationNotPermittedException("You cannot update others book shareable status");
        }
        book.setShareable(!book.isShareable());
        bookRepo.save(book);
        return bookId;
    }

    public Integer updateArchivedStatus(Integer bookId, Authentication connectedUser) {
        Books book =bookRepo.findById(bookId).orElseThrow(() -> new EntityNotFoundException("No book found with the ID:: "+bookId));
        User user = (User) connectedUser;
        if (!Objects.equals(book.getOwner().getUserId(),user.getUserId() )){
            throw new OperationNotPermittedException("You cannot update others book archived status");
        }
        book.setShareable(!book.isArchived());
        bookRepo.save(book);
        return bookId;
    }

    public Integer borrowBook(Integer bookId, Authentication connectedUser) {
        Books book =bookRepo.findById(bookId).orElseThrow(() -> new EntityNotFoundException("No book found with the ID:: "+bookId));
        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedException("The requested book cannot be borrowed since it is archived or not shareable");
        }

        User user = (User) connectedUser.getPrincipal();
        if (Objects.equals(book.getOwner().getUserId(),user.getUserId())) {
            throw new OperationNotPermittedException("You cannot borrow your own book");
        }

        final boolean isAlreadyBorrowed= historyRepository.isAlreadyBorrowedByUser(bookId,user.getUserId());
        if (isAlreadyBorrowed) {
            throw new OperationNotPermittedException("Book is already borrowed");
        }
        BookTransactionHistory bookTransactionHistory = BookTransactionHistory.builder()
                .user(user)
                .book(book)
                .returned(false)
                .returnApproved(false)
                .build();
        return historyRepository.save(bookTransactionHistory).getId();
    }

    public Integer returnedBorrowedBook(Integer bookId, Authentication connectedUser) {
        Books book =bookRepo.findById(bookId).orElseThrow(() -> new EntityNotFoundException("No book found with the ID:: "+bookId));
        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedException("The requested book cannot be borrowed since it is archived or not shareable");
        }
        User user = (User) connectedUser.getPrincipal();
        if (Objects.equals(book.getOwner().getUserId(),user.getUserId())) {
            throw new OperationNotPermittedException("You cannot borrow or return your own book");
        }

        BookTransactionHistory bookTransactionHistory = historyRepository.findByBookIdAndUserId(bookId,user.getUserId())
                .orElseThrow(()-> new OperationNotPermittedException("You did not borrow this book"));
        bookTransactionHistory.setReturned(true);
        return historyRepository.save(bookTransactionHistory).getId();
    }


    public Integer approveReturnedBorrowedBook(Integer bookId, Authentication connectedUser) {
        Books book =bookRepo.findById(bookId).orElseThrow(() -> new EntityNotFoundException("No book found with the ID:: "+bookId));
        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedException("The requested book cannot be borrowed since it is archived or not shareable");
        }
        User user = (User) connectedUser.getPrincipal();
        if (Objects.equals(book.getOwner().getUserId(),user.getUserId())) {
            throw new OperationNotPermittedException("You cannot borrow or return your own book");
        }

        BookTransactionHistory bookTransactionHistory = historyRepository.findByBookIdAndOwnerId(bookId,user.getUserId())
                .orElseThrow(()-> new OperationNotPermittedException("The book is not returned yet. You cannot approve its return"));
        bookTransactionHistory.setReturnApproved(true);
        return historyRepository.save(bookTransactionHistory).getId();

    }
}
