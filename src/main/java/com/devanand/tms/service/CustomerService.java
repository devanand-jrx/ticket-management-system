package com.devanand.tms.service;

import com.devanand.tms.contract.request.CustomerRequest;
import com.devanand.tms.contract.response.CustomerResponse;
import com.devanand.tms.exception.CustomerNotFoundException;
import com.devanand.tms.model.Customer;
import com.devanand.tms.repository.CustomerRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {
    @Autowired private final CustomerRepository customerRepository;

    private final ModelMapper modelMapper;

    public CustomerResponse createCustomer(CustomerRequest customerRequest) {
        log.info("Creating customer with email: {}", customerRequest.getEmail());
        Customer customer =
                Customer.builder()
                        .name(customerRequest.getName())
                        .email(customerRequest.getEmail())
                        .build();
        customer = customerRepository.save(customer);
        log.info("Customer created with ID: {}", customer.getId());
        return modelMapper.map(customer, CustomerResponse.class);
    }

    public List<CustomerResponse> getAllCustomers() {
        log.info("Retrieving all customers");
        return customerRepository.findAll().stream()
                .map(customer -> modelMapper.map(customer, CustomerResponse.class))
                .collect(Collectors.toList());
    }

    public CustomerResponse getCustomerById(Long id) {
        log.info("Retrieving customer with ID: {}", id);
        Customer customer =
                customerRepository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new CustomerNotFoundException(
                                                "Customer not found with id " + id));
        log.info("Customer found with ID: {}", id);
        return modelMapper.map(customer, CustomerResponse.class);
    }

    public CustomerResponse updateCustomer(Long id, CustomerRequest customerRequest) {
        log.info("Updating customer with ID: {}", id);
        Customer customer =
                customerRepository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new CustomerNotFoundException(
                                                "Customer not found with id " + id));
        modelMapper.map(customerRequest, customer);
        Customer updatedCustomer = customerRepository.save(customer);
        log.info("Customer updated with ID: {}", updatedCustomer.getId());
        return modelMapper.map(customerRepository.save(customer), CustomerResponse.class);
    }

    public void deleteCustomer(Long id) {
        log.info("Deleting customer with ID: {}", id);
        if (!customerRepository.existsById(id)) {
            log.error("Customer not found with ID: {}", id);
            throw new CustomerNotFoundException("Customer not found with id " + id);
        }
        log.info("Customer deleted with ID: {}", id);
        customerRepository.deleteById(id);
    }
}
