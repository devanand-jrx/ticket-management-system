package com.devanand.tms.service;

import com.devanand.tms.contract.request.CustomerRequest;
import com.devanand.tms.contract.response.CustomerResponse;
import com.devanand.tms.exception.CustomerNotFoundException;
import com.devanand.tms.model.Customer;
import com.devanand.tms.repository.CustomerRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {
    @Autowired private CustomerRepository customerRepository;

    private final ModelMapper modelMapper;

    public CustomerResponse createCustomer(CustomerRequest customerRequest) {
        Customer customer =
                Customer.builder()
                        .name(customerRequest.getName())
                        .email(customerRequest.getEmail())
                        .build();
        customer = customerRepository.save(customer);
        return modelMapper.map(customer, CustomerResponse.class);
    }

    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(customer -> modelMapper.map(customer, CustomerResponse.class))
                .collect(Collectors.toList());
    }

    public CustomerResponse getCustomerById(Long id) {
        Customer customer =
                customerRepository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new CustomerNotFoundException(
                                                "Customer not found with id " + id));
        return modelMapper.map(customer, CustomerResponse.class);
    }

    public CustomerResponse updateCustomer(Long id, CustomerRequest customerRequest) {
        Customer customer =
                customerRepository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new CustomerNotFoundException(
                                                "Customer not found with id " + id));
        modelMapper.map(customerRequest, customer);
        return modelMapper.map(customerRepository.save(customer), CustomerResponse.class);
    }

    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new CustomerNotFoundException("Customer not found with id " + id);
        }
        customerRepository.deleteById(id);
    }
}
