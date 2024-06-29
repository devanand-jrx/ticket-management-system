package com.devanand.tms.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.devanand.tms.contract.request.CustomerRequest;
import com.devanand.tms.contract.response.CustomerResponse;
import com.devanand.tms.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class CustomerControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockBean private CustomerService customerService;

    @Autowired private ObjectMapper objectMapper;

    @Test
    public void testCreateCustomer() throws Exception {
        CustomerRequest request = new CustomerRequest();
        CustomerResponse expectedResponse = new CustomerResponse();

        when(customerService.createCustomer(any(CustomerRequest.class)))
                .thenReturn(expectedResponse);

        mockMvc.perform(
                        post("/agents/customers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));
    }

    @Test
    public void testGetAllCustomers() throws Exception {
        List<CustomerResponse> expectedResponseList = new ArrayList<>();
        // Populate expectedResponseList with mock data

        when(customerService.getAllCustomers()).thenReturn(expectedResponseList);

        mockMvc.perform(get("/agents/customers"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponseList)));
    }

    @Test
    public void testGetCustomerById() throws Exception {
        Long id = 1L;
        CustomerResponse expectedResponse = new CustomerResponse(/* fill with expected data */ );

        when(customerService.getCustomerById(id)).thenReturn(expectedResponse);

        mockMvc.perform(get("/agents/customers/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));
    }

    @Test
    public void testUpdateCustomer() throws Exception {
        Long id = 1L;
        CustomerRequest request = new CustomerRequest(/* fill with necessary data */ );
        CustomerResponse expectedResponse = new CustomerResponse(/* fill with expected data */ );

        when(customerService.updateCustomer(eq(id), any(CustomerRequest.class)))
                .thenReturn(expectedResponse);

        mockMvc.perform(
                        put("/agents/customers/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));
    }

    @Test
    public void testDeleteCustomer() throws Exception {
        Long id = 1L;

        mockMvc.perform(delete("/agents/customers/{id}", id)).andExpect(status().isOk());

        verify(customerService, times(1)).deleteCustomer(eq(id));
    }
}
