package com.example.BookSocialNetwork.domain.bookTransactionHistory
        ;

import com.example.BookSocialNetwork.domain.book.Books;
import com.example.BookSocialNetwork.domain.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@Builder
public class BookTransactionHistory {
    @GeneratedValue
    private Integer id;

    @ManyToOne
    @JoinColumn(name ="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name ="book_id")
    private Books book;


    private boolean returned;
    private boolean returnApproved;

    @CreatedDate
    @Column(nullable = false,updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;

    @CreatedBy
    @Column(nullable = false,updatable = false)
    private Integer createdBy;

    @LastModifiedBy
    @Column(insertable = false)
    private Integer lastModifiedBy;

}
