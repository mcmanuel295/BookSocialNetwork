package com.example.BookSocialNetwork.repository;

import com.example.BookSocialNetwork.entities.Books;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Books,String> {

}
