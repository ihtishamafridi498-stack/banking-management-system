package com.afridi.bankmanagementsystem.repository;

import com.afridi.bankmanagementsystem.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByUsername(@NotBlank(message = "Username is required") @Size(min = 3, max = 20,
                message = "Username must be between 3 and 20 characters") String username);

    boolean existsByEmail(@NotBlank(message = "Email is required") @Email(message = "Invalid email format") String email);

   Optional<User> findByUsername(String userName);
}
