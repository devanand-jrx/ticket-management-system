package com.devanand.tms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.devanand.tms.contract.request.CustomerRequest;
import com.devanand.tms.contract.response.CustomerResponse;
import com.devanand.tms.exception.CustomerNotFoundException;
import com.devanand.tms.model.Customer;
import com.devanand.tms.repository.CustomerRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

public class CustomerServiceTest {

    @InjectMocks private CustomerService customerService;

    @Mock private CustomerRepository customerRepository;

    @Mock private ModelMapper modelMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCustomer() {
        CustomerRequest customerRequest = new CustomerRequest("Name", "name@example.com");
        Customer customer =
                Customer.builder()
                        .name(customerRequest.getName())
                        .email(customerRequest.getEmail())
                        .build();
        CustomerResponse expectedResponse =
                CustomerResponse.builder()
                        .id(1L)
                        .name(customer.getName())
                        .email(customer.getEmail())
                        .build();

        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(modelMapper.map(customer, CustomerResponse.class)).thenReturn(expectedResponse);

        CustomerResponse actualResponse = customerService.createCustomer(customerRequest);

        assertEquals(expectedResponse, actualResponse);
        verify(customerRepository, times(1)).save(any(Customer.class));
        verify(modelMapper, times(1)).map(customer, CustomerResponse.class);
    }

    @Test
    void testGetAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer());
        List<CustomerResponse> customerResponses = new ArrayList<>();
        customerResponses.add(new CustomerResponse());

        when(customerRepository.findAll()).thenReturn(customers);
        when(modelMapper.map(any(Customer.class), eq(CustomerResponse.class)))
                .thenReturn(customerResponses.get(0));

        List<CustomerResponse> result = customerService.getAllCustomers();

        assertEquals(customerResponses.size(), result.size());
        verify(customerRepository).findAll();
        verify(modelMapper, times(customers.size()))
                .map(any(Customer.class), eq(CustomerResponse.class));
    }

    @Test
    void testGetCustomerById() {
        Long customerId = 1L;
        Customer customer = new Customer();
        CustomerResponse customerResponse = new CustomerResponse();

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(modelMapper.map(customer, CustomerResponse.class)).thenReturn(customerResponse);

        CustomerResponse result = customerService.getCustomerById(customerId);

        assertEquals(customerResponse, result);
        verify(customerRepository).findById(customerId);
        verify(modelMapper).map(customer, CustomerResponse.class);
    }

    @Test
    void testGetCustomerById_ThrowsException() {
        Long customerId = 1L;

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        assertThrows(
                CustomerNotFoundException.class, () -> customerService.getCustomerById(customerId));
        verify(customerRepository).findById(customerId);
    }

    @Test
    void testUpdateCustomer() {
        Long customerId = 1L;
        CustomerRequest customerRequest =
                new CustomerRequest("Updated Name", "updated.email@example.com");
        Customer customer = new Customer();
        Customer updatedCustomer = new Customer();
        CustomerResponse customerResponse = new CustomerResponse();

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerRepository.save(customer)).thenReturn(updatedCustomer);
        when(modelMapper.map(updatedCustomer, CustomerResponse.class)).thenReturn(customerResponse);

        CustomerResponse result = customerService.updateCustomer(customerId, customerRequest);

        assertEquals(customerResponse, result);
        verify(customerRepository).findById(customerId);
        verify(customerRepository).save(customer);
        verify(modelMapper).map(customerRequest, customer);
        verify(modelMapper).map(updatedCustomer, CustomerResponse.class);
    }

    @Test
    void testUpdateCustomer_ThrowsException() {
        Long customerId = 1L;
        CustomerRequest customerRequest =
                new CustomerRequest("Updated Name", "updated.email@example.com");

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        assertThrows(
                CustomerNotFoundException.class,
                () -> customerService.updateCustomer(customerId, customerRequest));
        verify(customerRepository).findById(customerId);
    }

    @Test
    void testDeleteCustomer() {
        Long customerId = 1L;

        when(customerRepository.existsById(customerId)).thenReturn(true);

        customerService.deleteCustomer(customerId);

        verify(customerRepository).existsById(customerId);
        verify(customerRepository).deleteById(customerId);
    }

    @Test
    void testDeleteCustomer_ThrowsException() {
        Long customerId = 1L;

        when(customerRepository.existsById(customerId)).thenReturn(false);

        assertThrows(
                CustomerNotFoundException.class, () -> customerService.deleteCustomer(customerId));
        verify(customerRepository).existsById(customerId);
    }
}
