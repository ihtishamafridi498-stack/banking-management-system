#  Banking Management System

A RESTful Banking Management System built using **Java Spring Boot** that provides secure and efficient management of customers, bank accounts, and financial transactions.

The project follows a layered architecture using Controllers, Services, Repositories, DTOs, and Global Exception Handling to build a clean, maintainable, and scalable backend application.

---

##  Features

### Authentication
- User Registration
- User Login

### Customer Management
- Create Customer
- Get Customer by ID
- Get All Customers
- Update Customer Details
- Update Customer Status

### Account Management
- Create Bank Account
- View Account Details
- View Customer Accounts
- Update Account Status

### Transaction Management
- Deposit Money
- Withdraw Money
- Transfer Money Between Accounts

### Validation & Error Handling
- Bean Validation using Jakarta Validation
- Global Exception Handling
- Custom Error Response DTO
- Meaningful HTTP Status Codes

### API Documentation
- Swagger UI Integration

---

##  Tech Stack

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- PostgreSQL
- Maven
- Lombok
- Jakarta Validation
- Swagger / OpenAPI

---

##  Project Structure

```
src/main/java
│
├── controller
├── service
├── repository
├── entity
├── dto
│   ├── request
│   └── response
├── exception
├── enums
└── config
```

The application follows the following architecture:

```
Client
   │
   ▼
Controller
   │
   ▼
Service
   │
   ▼
Repository
   │
   ▼
PostgreSQL Database
```

---

##  Modules

- Authentication
- Customer
- Account
- Transaction

---

##  Transaction Operations

- Deposit
- Withdraw
- Transfer Between Accounts

Each transaction updates account balances while maintaining proper validation and exception handling.

---

## Validation

The project validates incoming requests using Jakarta Bean Validation.

Examples include:

- Required Fields
- Positive Amount Validation
- Initial Deposit Validation
- Invalid Request Handling

---

##  Exception Handling

Global Exception Handling has been implemented to provide consistent API responses.

Examples include:

- Resource Not Found
- Invalid Request
- Validation Errors
- Business Rule Violations

---

##  API Documentation

All REST APIs can be tested using Swagger UI after running the application.

---

##  Future Improvements (Increment 2)

The next version of this project will include:

- Spring Security
- JWT Authentication
- Role-Based Authorization
- Refresh Tokens
---

##  Author

**Ihtisham Afridi**

LinkedIn: *(www.linkedin.com/in/ihtisham-afridi-8856b63b7)*
