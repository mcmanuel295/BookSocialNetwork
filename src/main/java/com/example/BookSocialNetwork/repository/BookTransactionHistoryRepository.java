package com.example.BookSocialNetwork.repository;

import com.example.BookSocialNetwork.entities.BookTransactionHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookTransactionHistoryRepository extends JpaRepository<BookTransactionHistory,Integer> {
    @Query("SELECT h FROM BookTransactionHistory WHERE h.user.id = :userId")
    Page<BookTransactionHistory> findAllBorrowedBooks(Pageable pageable, Integer userId);
}
