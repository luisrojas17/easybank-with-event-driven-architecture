package com.easybank.common.command;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Builder
@Data
public class UpdateCardMobileNumberCommand {

    @TargetAggregateIdentifier
    private Long cardNumber;
    private String customerId;
    private Long accountNumber;
    private Long loanNumber;
    private String currentMobileNumber;
    private String newMobileNumber;
}
