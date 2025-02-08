package com.example.BookSocialNetwork.repository;

import com.example.BookSocialNetwork.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book,String> {
}
