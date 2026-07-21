package com.afridi.bankmanagementsystem.service;

import com.afridi.bankmanagementsystem.requestdto.CustomerStatusUpdateRequestDto;
import com.afridi.bankmanagementsystem.requestdto.UpdateCustomerRequestDto;
import com.afridi.bankmanagementsystem.responsedto.CustomerResponseDto;

import java.util.List;

public interface CustomerService {

    List<CustomerResponseDto> getAllCustomers();

    CustomerResponseDto getCustomerById(Long customerId);

    CustomerResponseDto updateCustomer(UpdateCustomerRequestDto requestDto,Long customerId);

    CustomerResponseDto updateStatus(Long customerId, CustomerStatusUpdateRequestDto requestDt0);
}
