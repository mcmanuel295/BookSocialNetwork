package com.example.BookSocialNetwork.model;

import com.example.BookSocialNetwork.entities.Books;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecification {

    public static Specification<Books> withOwner(Integer ownerId){
        return (root,query,criteriaBuilder)-> criteriaBuilder.equal(root.get("owner").get("id"),ownerId);
    }
}
