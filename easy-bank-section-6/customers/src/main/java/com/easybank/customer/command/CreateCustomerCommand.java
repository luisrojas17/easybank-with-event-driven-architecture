package com.easybank.customer.command;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@Builder
public class CreateCustomerCommand {

    // To tell to Axon that the annotated field is an id of a given aggregate to which the command should be targeted.
    @TargetAggregateIdentifier
    private final String customerId;
    private final String name;
    private final String email;
    private final String mobileNumber;
    private final boolean activeSw;


}
