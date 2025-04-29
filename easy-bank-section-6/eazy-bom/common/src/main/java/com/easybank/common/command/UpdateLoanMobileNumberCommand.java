package com.easybank.common.command;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Builder
@Data
public class UpdateLoanMobileNumberCommand {

    @TargetAggregateIdentifier
    private Long loanNumber;
    private String customerId;
    private Long accountNumber;
    private Long cardNumber;
    private String currentMobileNumber;
    private String newMobileNumber;

}
