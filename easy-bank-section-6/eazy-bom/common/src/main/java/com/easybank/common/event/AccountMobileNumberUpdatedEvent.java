package com.easybank.common.event;

import lombok.Data;

@Data
public class AccountMobileNumberUpdatedEvent {

    private String customerId;
    private Long accountNumber;
    private Long cardNumber;
    private Long loanNumber;
    private String currentMobileNumber;
    private String newMobileNumber;
}
