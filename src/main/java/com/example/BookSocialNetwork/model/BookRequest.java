package com.example.BookSocialNetwork.model;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BookRequest(
        @NotNull(message = "100")
        @NotBlank(message = "100")
        Integer id,

        @NotNull(message = "101")
        @NotBlank(message = "101")
        String title,

        @NotNull(message = "102")
        @NotBlank(message = "102")
        String authorName,

        @NotNull(message = "103")
        @NotBlank(message = "103")
        String isbn,

        @NotNull(message = "104")
        @NotBlank(message = "104")
        String synopsis,


        @NotNull(message = "105")
        @NotBlank(message = "105")
        boolean shareable
) {

}
