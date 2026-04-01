package com.example.BookSocialNetwork.repository;

import com.example.BookSocialNetwork.entities.BookTransactionHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BookTransactionHistoryRepository extends JpaRepository<BookTransactionHistory,Integer> {
    @Query("SELECT h FROM BookTransactionHistory WHERE h.user.id = :userId")
    Page<BookTransactionHistory> findAllBorrowedBooks(Pageable pageable, Integer userId);

    @Query("SELECT h FROM BookTransactionHistory WHERE h.book.owner.id = :userId")
    Page<BookTransactionHistory> findAllReturnedBooks(Pageable pageable, Integer userId);

    @Query(""" 
            SELECT (COUNT(*) AS isBorrowed
            FROM BookTransactionHistory b
            WHERE b.user.id = :userId
            AND b.book.id= :bookId
            AND b.returnApproved = false
            """)
    boolean isAlreadyBorrowedByUser(Integer bookId, Integer userId);

    @Query(""" 
         SELECT t FROM BookTransactionHistory t
         WHERE t.user.id = :userId
         AND t.book.id =:bookId
         AND t.returned =false
         AND t.returnedApproved =false
        """)
    Optional<BookTransactionHistory> findByBookIdAndUserId(Integer bookId, Integer userId);

    @Query("""
            SELECT t FROM BookTransactionHistory t
         WHERE t.book.owner.id = :userId
         AND t.book.id =:bookId
         AND t.returned = true
         AND t.returnedApproved = false
         """)
    Optional<BookTransactionHistory> findByBookIdAndOwnerId(Integer bookId, Integer userId);
}
