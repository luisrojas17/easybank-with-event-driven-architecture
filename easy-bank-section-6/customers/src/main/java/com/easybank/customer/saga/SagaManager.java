package com.easybank.customer.saga;

import com.easybank.common.command.RollbackCustomerMobileNumberCommand;
import com.easybank.common.command.UpdateAccountMobileNumberCommand;
import com.easybank.common.event.CustomerMobileNumberUpdatedEvent;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.CommandResultMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This class acts as a saga manager.
 * It is used to manage the saga's lifecycle.
 */
@Slf4j
@Saga
public class SagaManager {

    @Autowired
    private transient CommandGateway commandGateway;

    @StartSaga
    @SagaEventHandler(associationProperty = "customerId")
    public void handler(CustomerMobileNumberUpdatedEvent customerMobileNumberUpdatedEvent) {

        log.info("Saga event 1 [Start]: Receiving CustomerMobileNumberUpdatedEvent for customerId [{}].",
                customerMobileNumberUpdatedEvent.getCustomerId());

        UpdateAccountMobileNumberCommand updateAccountMobileNumberCommand = UpdateAccountMobileNumberCommand.builder()
                .customerId(customerMobileNumberUpdatedEvent.getCustomerId())
                .accountNumber(customerMobileNumberUpdatedEvent.getAccountNumber())
                .cardNumber(customerMobileNumberUpdatedEvent.getCardNumber())
                .loanNumber(customerMobileNumberUpdatedEvent.getLoanNumber())
                .currentMobileNumber(customerMobileNumberUpdatedEvent.getCurrentMobileNumber())
                .newMobileNumber(customerMobileNumberUpdatedEvent.getNewMobileNumber())
                .build();

        // To send command to update mobile number in Accounts Microservice
        // So, you have to receive and process this command in accounts aggregate Microservice
        commandGateway.send(updateAccountMobileNumberCommand, new CommandCallback<>() {
            @Override
            public void onResult(
                    @NonNull CommandMessage<? extends UpdateAccountMobileNumberCommand> commandMessage,
                    @NonNull CommandResultMessage<?> commandResultMessage) {

                if(commandResultMessage.isExceptional()) {

                    log.info("Saga event 1 [Start]: Publishing RollbackCustomerMobileNumberUpdatedEvent for customerId [{}].",
                            customerMobileNumberUpdatedEvent.getCustomerId());

                    RollbackCustomerMobileNumberCommand rollbackCustomerMobileNumberCommand =
                            RollbackCustomerMobileNumberCommand.builder()
                                    .customerId(customerMobileNumberUpdatedEvent.getCustomerId())
                                    .currentMobileNumber(customerMobileNumberUpdatedEvent.getCurrentMobileNumber())
                                    .newMobileNumber(customerMobileNumberUpdatedEvent.getNewMobileNumber())
                                    .errorMessage(commandResultMessage.exceptionResult().getMessage())
                                    .build();

                    // To send command to rollback mobile number in Customers Microservice
                    commandGateway.sendAndWait(rollbackCustomerMobileNumberCommand);

                }

            }

        });

        log.info("Saga event 1 [End]: CustomerMobileNumberUpdatedEvent for customerId [{}] completed successfully.",
                customerMobileNumberUpdatedEvent.getCustomerId());
    }
}
