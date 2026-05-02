package com.example.BookSocialNetwork.domain.repository;

import com.example.BookSocialNetwork.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Integer> {

    Optional<Role> findByName(String role);
}
