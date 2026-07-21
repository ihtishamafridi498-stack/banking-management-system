package com.afridi.bankmanagementsystem.model;

import com.afridi.bankmanagementsystem.enums.CustomerStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "customers")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;
    private String customerName;
    private String phone;
    @Column(unique = true, nullable = false)
    private String customerCnic;
    private String address;
    @OneToMany(mappedBy = "customer")
    private List<Account> accountList;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Enumerated(EnumType.STRING)
    private CustomerStatus customerStatus;
}
