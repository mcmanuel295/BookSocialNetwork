package com.example.BookSocialNetwork.repository;

import com.example.BookSocialNetwork.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book,String> {
}
