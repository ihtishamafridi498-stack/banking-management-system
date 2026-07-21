package com.afridi.bankmanagementsystem.repository;

import com.afridi.bankmanagementsystem.model.Customer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
    boolean existsByCustomerCnic(@NotBlank(message = "CNIC is required") @Pattern(
                regexp = "\\d{5}-\\d{7}-\\d",
                message = "CNIC must be in format 12345-1234567-1"
        ) String s);
}
