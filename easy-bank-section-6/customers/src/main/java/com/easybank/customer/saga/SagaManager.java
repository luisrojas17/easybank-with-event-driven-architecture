package com.easybank.customer.saga;

import com.easybank.common.command.*;
import com.easybank.common.event.*;
import com.easybank.customer.constants.CustomerConstants;
import com.easybank.customer.dto.ResponseDto;
import com.easybank.customer.query.FindCustomerQuery;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.CommandResultMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;

/**
 * This class acts as a saga manager.
 * It is used to manage the saga's lifecycle.
 */
@Slf4j
@Saga
public class SagaManager {

    @Autowired
    private transient CommandGateway commandGateway;

    /**
     * To emit a response to subscription query.
     * Got to:
     * https://docs.axoniq.io/axon-framework-reference/4.11/queries/
     */
    @Autowired
    private transient QueryUpdateEmitter queryUpdateEmitter;

    @StartSaga
    @SagaEventHandler(associationProperty = "customerId")
    public void handler(CustomerMobileNumberUpdatedEvent customerMobileNumberUpdatedEvent) {

        log.info("Saga event 1 [Start]: Receiving CustomerMobileNumberUpdatedEvent for customerId [{}].",
                customerMobileNumberUpdatedEvent.getCustomerId());

        // To create a new command for next step. In this case, to update mobile number in Accounts Microservice
        UpdateAccountMobileNumberCommand updateAccountMobileNumberCommand =
                UpdateAccountMobileNumberCommand.builder()
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

            // If there is an exception, then send a command to rollback mobile number in Customers Microservice
            @Override
            public void onResult(
                    @NonNull CommandMessage<? extends UpdateAccountMobileNumberCommand> commandMessage,
                    @NonNull CommandResultMessage<?> commandResultMessage) {

                if(commandResultMessage.isExceptional()) {

                    log.info("Saga event 1 [Start]: Sending RollbackCustomerMobileNumberCommand for customerId [{}].",
                            customerMobileNumberUpdatedEvent.getCustomerId());

                    RollbackCustomerMobileNumberCommand rollbackCustomerMobileNumberCommand =
                            RollbackCustomerMobileNumberCommand.builder()
                                    .customerId(customerMobileNumberUpdatedEvent.getCustomerId())
                                    .currentMobileNumber(customerMobileNumberUpdatedEvent.getCurrentMobileNumber())
                                    .newMobileNumber(customerMobileNumberUpdatedEvent.getNewMobileNumber())
                                    .errorMessage(commandResultMessage.exceptionResult().getMessage())
                                    .build();

                    // To send command to rollback mobile number in Customers Microservice
                    // This command is handled in customers aggregate
                    commandGateway.sendAndWait(rollbackCustomerMobileNumberCommand);

                    log.info("Saga event 1 [End]: RollbackCustomerMobileNumberCommand sent successfully for customerId [{}].",
                            customerMobileNumberUpdatedEvent.getCustomerId());

                }

            }

        });

        log.info("Saga event 1 [End]: CustomerMobileNumberUpdatedEvent for customerId [{}] completed successfully.",
                customerMobileNumberUpdatedEvent.getCustomerId());
    }

    @SagaEventHandler(associationProperty = "customerId")
    public void handler(AccountMobileNumberUpdatedEvent accountMobileNumberUpdatedEvent) {

        log.info("Saga event 2 [Start]: Receiving AccountMobileNumberUpdatedEvent for accountNumber [{}].",
                accountMobileNumberUpdatedEvent.getAccountNumber());

        // To create a new command for next step. In this case, to update mobile number in Cards Microservice
        UpdateCardMobileNumberCommand updateCardMobileNumberCommand =
                UpdateCardMobileNumberCommand.builder()
                        .customerId(accountMobileNumberUpdatedEvent.getCustomerId())
                        .accountNumber(accountMobileNumberUpdatedEvent.getAccountNumber())
                        .cardNumber(accountMobileNumberUpdatedEvent.getCardNumber())
                        .loanNumber(accountMobileNumberUpdatedEvent.getLoanNumber())
                        .currentMobileNumber(accountMobileNumberUpdatedEvent.getCurrentMobileNumber())
                        .newMobileNumber(accountMobileNumberUpdatedEvent.getNewMobileNumber())
                        .build();

        // To send command to update mobile number in Cards Microservice
        // So, you have to receive and process this command in cards aggregate Microservice
        commandGateway.send(updateCardMobileNumberCommand, new CommandCallback<>() {

            // If there is an exception, then send a command to rollback mobile number in Accounts Microservice
            @Override
            public void onResult(@Nonnull CommandMessage<? extends UpdateCardMobileNumberCommand> commandMessage,
                                 @Nonnull CommandResultMessage<?> commandResultMessage) {

                if(commandResultMessage.isExceptional()) {

                    log.info("Saga event 2 [Start]: Sending RollbackAccountMobileNumberCommand for accountNumber [{}].",
                            accountMobileNumberUpdatedEvent.getAccountNumber());

                    RollbackAccountMobileNumberCommand rollbackAccountMobileNumberCommand =
                            RollbackAccountMobileNumberCommand.builder()
                                    .customerId(accountMobileNumberUpdatedEvent.getCustomerId())
                                    .accountNumber(accountMobileNumberUpdatedEvent.getAccountNumber())
                                    .currentMobileNumber(accountMobileNumberUpdatedEvent.getCurrentMobileNumber())
                                    .newMobileNumber(accountMobileNumberUpdatedEvent.getNewMobileNumber())
                                    .errorMessage(commandResultMessage.exceptionResult().getMessage())
                                    .build();

                    // To send command to rollback mobile number in Accounts Microservice
                    // This command is handled in accounts aggregate
                    commandGateway.sendAndWait(rollbackAccountMobileNumberCommand);

                    log.info("Saga event 2 [End]: RollbackAccountMobileNumberCommand sent successfully for accountNumber [{}].",
                            accountMobileNumberUpdatedEvent.getAccountNumber());
                }

            }
        });

        log.info("Saga event 2 [End]: AccountMobileNumberUpdatedEvent for accountNumber [{}] completed successfully.",
                accountMobileNumberUpdatedEvent.getAccountNumber());
    }

    @SagaEventHandler(associationProperty = "customerId")
    public void handler(CardMobileNumberUpdatedEvent cardMobileNumberUpdatedEvent) {

        log.info("Saga event 3 [Start]: Receiving CardMobileNumberUpdatedEvent for cardNumber [{}].",
                cardMobileNumberUpdatedEvent.getCardNumber());

        // To create a new command for next step. In this case, to update mobile number in Loans Microservice
        UpdateLoanMobileNumberCommand updateLoanMobileNumberCommand =
                UpdateLoanMobileNumberCommand.builder()
                        .customerId(cardMobileNumberUpdatedEvent.getCustomerId())
                        .accountNumber(cardMobileNumberUpdatedEvent.getAccountNumber())
                        .cardNumber(cardMobileNumberUpdatedEvent.getCardNumber())
                        .loanNumber(cardMobileNumberUpdatedEvent.getLoanNumber())
                        .currentMobileNumber(cardMobileNumberUpdatedEvent.getCurrentMobileNumber())
                        .newMobileNumber(cardMobileNumberUpdatedEvent.getNewMobileNumber())
                        .build();

        // To send command to update mobile number in Loans Microservice
        // So, you have to receive and process this command in loans aggregate Microservice
        commandGateway.send(updateLoanMobileNumberCommand, new CommandCallback<>() {

            // If there is an exception, then send a command to rollback mobile number in Cards Microservice
            @Override
            public void onResult(@Nonnull CommandMessage<? extends UpdateLoanMobileNumberCommand> commandMessage,
                                 @Nonnull CommandResultMessage<?> commandResultMessage) {

                if (commandResultMessage.isExceptional()) {

                    log.info("Saga event 3 [Start]: Sending RollbackCardMobileNumberCommand for cardNumber [{}].",
                            cardMobileNumberUpdatedEvent.getCardNumber());

                    RollbackCardMobileNumberCommand rollbackCardMobileNumberCommand =
                            RollbackCardMobileNumberCommand.builder()
                                    .customerId(cardMobileNumberUpdatedEvent.getCustomerId())
                                    .accountNumber(cardMobileNumberUpdatedEvent.getAccountNumber())
                                    .cardNumber(cardMobileNumberUpdatedEvent.getCardNumber())
                                    .currentMobileNumber(cardMobileNumberUpdatedEvent.getCurrentMobileNumber())
                                    .newMobileNumber(cardMobileNumberUpdatedEvent.getNewMobileNumber())
                                    .errorMessage(commandResultMessage.exceptionResult().getMessage())
                                    .build();

                    // To send command to rollback mobile number in Cards Microservice
                    // This command is handled in cards aggregate
                    commandGateway.sendAndWait(rollbackCardMobileNumberCommand);

                    log.info("Saga event 3 [End]: RollbackCardMobileNumberCommand sent successfully for cardNumber [{}].",
                            cardMobileNumberUpdatedEvent.getCardNumber());

                }

            }
        });

        log.info("Saga event 3 [End]: CardMobileNumberUpdatedEvent for cardNumber [{}] completed successfully.",
                cardMobileNumberUpdatedEvent.getCardNumber());
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "customerId")
    public void handler(LoanMobileNumberUpdatedEvent loanMobileNumberUpdatedEvent) {

        log.info("Saga event 4 [Start]: Receiving LoanMobileNumberUpdatedEvent for loanNumber [{}].",
                loanMobileNumberUpdatedEvent.getLoanNumber());

        queryUpdateEmitter.emit(FindCustomerQuery.class, query -> true,
                new ResponseDto(CustomerConstants.STATUS_200,
                        CustomerConstants.MOBILE_NUMBER_UPDATE_SUCCESS_MSG));

        log.info("Saga event 4 [End]: LoanMobileNumberUpdatedEvent for loanNumber [{}] completed successfully.",
                loanMobileNumberUpdatedEvent.getLoanNumber());
    }

    // To handle all events published by the aggregate (CustomerAggregate, AccountAggregate, CardAggregate) in order to make rollback
    @SagaEventHandler(associationProperty = "customerId")
    public void handler(CardMobileNumberRollbackedEvent cardMobileNumberRollbackedEvent) {

        log.info("Saga compensation event [Start]: Receiving CardMobileNumberRollbackedEvent for cardNumber [{}].",
                cardMobileNumberRollbackedEvent.getCardNumber());

        RollbackAccountMobileNumberCommand rollbackAccountMobileNumberCommand =
                RollbackAccountMobileNumberCommand.builder()
                        .customerId(cardMobileNumberRollbackedEvent.getCustomerId())
                        .accountNumber(cardMobileNumberRollbackedEvent.getAccountNumber())
                        .currentMobileNumber(cardMobileNumberRollbackedEvent.getCurrentMobileNumber())
                        .newMobileNumber(cardMobileNumberRollbackedEvent.getNewMobileNumber())
                        .errorMessage(cardMobileNumberRollbackedEvent.getErrorMessage())
                        .build();

        commandGateway.send(rollbackAccountMobileNumberCommand);

        log.info("Saga compensation event processed successfully. RollbackAccountMobileNumberCommand sent [{}].",
                rollbackAccountMobileNumberCommand);
    }

    @SagaEventHandler(associationProperty = "customerId")
    public void handler(AccountMobileNumberRollbackedEvent accountMobileNumberRollbackedEvent) {

        log.info("Saga compensation event: Receiving AccountMobileNumberRollbackedEvent for accountNumber [{}].",
                accountMobileNumberRollbackedEvent.getAccountNumber());

        RollbackCustomerMobileNumberCommand rollbackCustomerMobileNumberCommand =
                RollbackCustomerMobileNumberCommand.builder()
                        .customerId(accountMobileNumberRollbackedEvent.getCustomerId())
                        .currentMobileNumber(accountMobileNumberRollbackedEvent.getCurrentMobileNumber())
                        .newMobileNumber(accountMobileNumberRollbackedEvent.getNewMobileNumber())
                        .errorMessage(accountMobileNumberRollbackedEvent.getErrorMessage())
                        .build();

        commandGateway.send(rollbackCustomerMobileNumberCommand);

        log.info("Saga compensation event processed successfully. AccountMobileNumberRollbackedEvent sent [{}].",
                rollbackCustomerMobileNumberCommand);
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "customerId")
    public void handler(CustomerMobileNumberRollbackedEvent customerMobileNumberRollbackedEvent) {

        log.info("Saga compensation event [End]: Receiving CustomerMobileNumberRollbackedEvent for customerId [{}].",
                customerMobileNumberRollbackedEvent.getCustomerId());

        queryUpdateEmitter.emit(FindCustomerQuery.class, query -> true,
                new ResponseDto(CustomerConstants.STATUS_500,
                        CustomerConstants.MOBILE_NUMBER_UPDATE_FAILURE_MSG));

        log.info("Saga compensation event processed successfully.");

    }
}
