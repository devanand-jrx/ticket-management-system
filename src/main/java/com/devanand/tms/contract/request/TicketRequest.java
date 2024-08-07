package com.devanand.tms.contract.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TicketRequest {
    @NotBlank(message = "Description cannot be empty")
    private String description;

    private Long agentId;
    private Long customerId;
}
