package com.easybank.common.command;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Builder
@Data
public class UpdateAccountMobileNumberCommand {

    @TargetAggregateIdentifier
    private Long accountNumber;
    private String customerId;
    private Long cardNumber;
    private Long loanNumber;
    private String currentMobileNumber;
    private String newMobileNumber;
}

