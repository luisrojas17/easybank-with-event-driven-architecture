package com.easybank.common.event;

import lombok.Data;

@Data
public class CardMobileNumberRollbackedEvent {

    private String customerId;
    private Long accountNumber;
    private Long cardNumber;
    private String currentMobileNumber;
    private String newMobileNumber;
    private String errorMessage;
}
