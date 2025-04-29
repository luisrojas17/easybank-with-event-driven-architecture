package com.easybank.gatewayserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerSummaryDto {

    private CustomerDto customer;
    private AccountDto account;
    private LoanDto loan;
    private CardDto card;

}
