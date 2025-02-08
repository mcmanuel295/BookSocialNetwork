package com.example.BookSocialNetwork.repository;

import com.example.BookSocialNetwork.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Integer> {

    Optional<Role> findByName(String role);
}
