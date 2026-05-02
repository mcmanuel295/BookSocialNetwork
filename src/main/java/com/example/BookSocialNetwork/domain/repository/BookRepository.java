package com.example.BookSocialNetwork.domain.repository;

import com.example.BookSocialNetwork.entities.Books;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Books,Integer>, JpaSpecificationExecutor<Books> {

    @Query("SELECT b FROM Books b WHERE b.archived = false AND b.shareable = true AND b.owner.id !=:userId")
    Page<Books> findAllDisplayableBooks(Pageable pageable, Integer userId);
}
