package com.afridi.bankmanagementsystem.controller;

import com.afridi.bankmanagementsystem.requestdto.CustomerStatusUpdateRequestDto;
import com.afridi.bankmanagementsystem.requestdto.UpdateCustomerRequestDto;
import com.afridi.bankmanagementsystem.responsedto.CustomerResponseDto;
import com.afridi.bankmanagementsystem.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<List<CustomerResponseDto>> getAllCustomers(){
       List<CustomerResponseDto> responseDtos=customerService.getAllCustomers();
       return ResponseEntity.status(HttpStatus.OK).body(responseDtos);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerResponseDto> getCustomerById(@PathVariable Long customerId){
        CustomerResponseDto responseDto=customerService.getCustomerById(customerId);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerResponseDto> updateCustomer( @Valid @RequestBody UpdateCustomerRequestDto requestDto ,@PathVariable Long customerId){
        CustomerResponseDto responseDto=customerService.updateCustomer(requestDto,customerId);
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/{customerId}/status")
    public ResponseEntity<CustomerResponseDto> updateStatus( @PathVariable Long customerId, @RequestBody CustomerStatusUpdateRequestDto requestDt0){
        CustomerResponseDto responseDto=customerService.updateStatus(customerId,requestDt0);
        return ResponseEntity.ok(responseDto);
    }
}
