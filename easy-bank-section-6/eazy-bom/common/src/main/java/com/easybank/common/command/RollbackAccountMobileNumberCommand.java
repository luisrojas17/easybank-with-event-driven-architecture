package com.easybank.common.command;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Builder
@Data
public class RollbackAccountMobileNumberCommand {

    @TargetAggregateIdentifier
    private Long accountNumber;
    private String customerId;
    private String currentMobileNumber;
    private String newMobileNumber;
    private String errorMessage;
}

