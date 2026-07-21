package com.afridi.bankmanagementsystem.serviceimpl;

import com.afridi.bankmanagementsystem.enums.Role;
import com.afridi.bankmanagementsystem.exception.*;
import com.afridi.bankmanagementsystem.model.Customer;
import com.afridi.bankmanagementsystem.model.User;
import com.afridi.bankmanagementsystem.repository.CustomerRepository;
import com.afridi.bankmanagementsystem.repository.UserRepository;
import com.afridi.bankmanagementsystem.requestdto.LoginRequestDto;
import com.afridi.bankmanagementsystem.requestdto.RegisterRequestDto;
import com.afridi.bankmanagementsystem.responsedto.LoginResponseDto;
import com.afridi.bankmanagementsystem.responsedto.RegisterResponseDto;
import com.afridi.bankmanagementsystem.responsedto.UserResponseDto;
import com.afridi.bankmanagementsystem.service.AuthService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;

    @Override
    public RegisterResponseDto registerUser(RegisterRequestDto registerRequestDto){

        if (userRepository.existsByUsername(
               registerRequestDto.username())) {

            throw new UsernameAlreadyExistsException(
                    "Username '" + registerRequestDto.username() + "' already exists.");
        }

        if (userRepository.existsByEmail(
                registerRequestDto.email())) {

            throw new EmailAlreadyExistsException(
                    "Email '" + registerRequestDto.email() + "' already exists.");
        }
        if (customerRepository.existsByCustomerCnic(
                registerRequestDto.customerCnic())) {

            throw new CustomerCnicAlreadyExistsException(
                    "CNIC '" + registerRequestDto.customerCnic() + "' already exists.");
        }
        User user =new User();
        user.setUsername(registerRequestDto.username());
        user.setEmail(registerRequestDto.email());
        user.setPassword(registerRequestDto.password());
        user.setRole(Role.CUSTOMER);
        User savedUser=userRepository.save(user);

        Customer customer=new Customer();
        customer.setCustomerName(registerRequestDto.customerName());
        customer.setPhone(registerRequestDto.phone());
        customer.setCustomerCnic(registerRequestDto.customerCnic());
        customer.setAddress(registerRequestDto.address());
        customer.setUser(savedUser);
        Customer savedCustomer=customerRepository.save(customer);

        return new RegisterResponseDto(
                savedUser.getUserId(),
                savedCustomer.getCustomerId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getRole().name(),
                "user registered successfully"
        );
    }

    @Override
    public LoginResponseDto loginUser(LoginRequestDto loginRequestDto){

        User user= userRepository.findByUsername(loginRequestDto.username())
                .orElseThrow(()->
                        new InvalidCredentialsException("invalid username or password"));
        if(!user.getPassword().equals(loginRequestDto.password())){
            throw new InvalidCredentialsException("invalid username or password");
        }

        return new LoginResponseDto(
                user.getUserId(),
                user.getUsername(),
                user.getRole().name(),
                "login successful"
        );
    }
    @Override
  public UserResponseDto getUserById(Long userId){
     User user=userRepository.findById(userId)
             .orElseThrow(()->
                     new UserNotFoundException("user not found"));
     return new UserResponseDto(
             user.getUserId(),
             user.getUsername(),
             user.getEmail(),
             user.getRole().name()
     );
    }
}
