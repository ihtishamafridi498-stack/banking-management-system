package com.afridi.bankmanagementsystem.serviceimpl;

import com.afridi.bankmanagementsystem.enums.AccountStatus;
import com.afridi.bankmanagementsystem.enums.CustomerStatus;
import com.afridi.bankmanagementsystem.enums.UserStatus;
import com.afridi.bankmanagementsystem.exception.CustomerHasActiveAccountsException;
import com.afridi.bankmanagementsystem.exception.CustomerNotFoundException;
import com.afridi.bankmanagementsystem.model.Customer;
import com.afridi.bankmanagementsystem.repository.CustomerRepository;
import com.afridi.bankmanagementsystem.requestdto.CustomerStatusUpdateRequestDto;
import com.afridi.bankmanagementsystem.requestdto.UpdateCustomerRequestDto;
import com.afridi.bankmanagementsystem.responsedto.CustomerResponseDto;
import com.afridi.bankmanagementsystem.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public List<CustomerResponseDto> getAllCustomers(){
    List<Customer> customers=customerRepository.findAll();
    List<CustomerResponseDto> responseDtos=new ArrayList<>();
     for(Customer customer: customers){
         CustomerResponseDto dto=new CustomerResponseDto(
                 customer.getCustomerId(),
                 customer.getCustomerName(),
                 customer.getUser().getEmail(),
                 customer.getPhone(),
                 customer.getCustomerCnic(),
                 customer.getAddress(),
                 customer.getCustomerStatus()
         );
         responseDtos.add(dto);
     }
     return responseDtos;
    }

    @Override
    public CustomerResponseDto getCustomerById(Long customerId) {
        Customer customer=customerRepository.findById(customerId)
                .orElseThrow(()->
                        new CustomerNotFoundException("customer not found with ID: "+customerId));
        return new CustomerResponseDto(
                customer.getCustomerId(),
                customer.getCustomerName(),
                customer.getUser().getEmail(),
                customer.getPhone(),
                customer.getCustomerCnic(),
                customer.getAddress(),
                customer.getCustomerStatus()
        );
    }

    @Override
 public CustomerResponseDto updateCustomer(UpdateCustomerRequestDto requestDto,Long customerId){
        Customer customer=customerRepository.findById(customerId)
                .orElseThrow(()->
                        new CustomerNotFoundException("customer not found with ID:"+ customerId));

       customer.setCustomerName(requestDto.customerName());
       customer.setPhone(requestDto.phone());
       customer.setCustomerCnic(requestDto.customerCnic());
       customer.setAddress(requestDto.address());

        Customer updatedCustomer=customerRepository.save(customer);

        return new CustomerResponseDto(
                updatedCustomer.getCustomerId(),
                updatedCustomer.getCustomerName(),
                updatedCustomer.getUser().getEmail(),
                updatedCustomer.getPhone(),
                updatedCustomer.getCustomerCnic(),
                updatedCustomer.getAddress(),
                updatedCustomer.getCustomerStatus()
        );
    }

  @Override
   public CustomerResponseDto updateStatus(Long customerId, CustomerStatusUpdateRequestDto requestDto){

          Customer customer = customerRepository.findById(customerId)
                  .orElseThrow(() ->
                          new CustomerNotFoundException(
                                  "Customer not found with ID: " + customerId));

          if (requestDto.status() == CustomerStatus.INACTIVE) {

              boolean hasNonClosedAccounts = customer.getAccountList().stream()
                      .anyMatch(account ->
                              account.getAccountStatus() != AccountStatus.CLOSED);

              if (hasNonClosedAccounts) {
                  throw new CustomerHasActiveAccountsException(
                          "Customer with ID " + customerId +
                                  " cannot be deactivated because one or more accounts are still active or frozen.");
              }

              customer.getUser().setUserStatus(UserStatus.DISABLED);
          }

          if (requestDto.status() == CustomerStatus.ACTIVE) {
              customer.getUser().setUserStatus(UserStatus.ACTIVE);
          }

          customer.setCustomerStatus(requestDto.status());

          Customer updatedCustomer = customerRepository.save(customer);

          return new CustomerResponseDto(
                  updatedCustomer.getCustomerId(),
                  updatedCustomer.getCustomerName(),
                  updatedCustomer.getUser().getEmail(),
                  updatedCustomer.getPhone(),
                  updatedCustomer.getCustomerCnic(),
                  updatedCustomer.getAddress(),
                  updatedCustomer.getCustomerStatus()
          );
      }
    }