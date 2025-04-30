package com.easybank.account.command.aggregate;

import com.easybank.account.command.CreateAccountCommand;
import com.easybank.account.command.DeleteAccountCommand;
import com.easybank.account.command.UpdateAccountCommand;
import com.easybank.account.command.event.AccountCreatedEvent;
import com.easybank.account.command.event.AccountDeletedEvent;
import com.easybank.account.command.event.AccountUpdatedEvent;
import com.easybank.common.command.RollbackAccountMobileNumberCommand;
import com.easybank.common.command.UpdateAccountMobileNumberCommand;
import com.easybank.common.event.AccountMobileNumberRollbackedEvent;
import com.easybank.common.event.AccountMobileNumberUpdatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

@Slf4j
@Aggregate
public class AccountAggregate {

    @AggregateIdentifier
    private Long accountNumber;
    private String mobileNumber;
    private String accountType;
    private String branchAddress;
    private boolean activeSw;
    private String errorMessage;

    public AccountAggregate(){}

    @CommandHandler
    public AccountAggregate(CreateAccountCommand createAccountCommand) {

        AccountCreatedEvent accountCreatedEvent = new AccountCreatedEvent();
        BeanUtils.copyProperties(createAccountCommand, accountCreatedEvent);

        AggregateLifecycle.apply(accountCreatedEvent);

    }

    @EventSourcingHandler
    public void on(AccountCreatedEvent accountCreatedEvent) {
        this.accountNumber = accountCreatedEvent.getAccountNumber();
        this.mobileNumber = accountCreatedEvent.getMobileNumber();
        this.accountType = accountCreatedEvent.getAccountType();
        this.branchAddress = accountCreatedEvent.getBranchAddress();
        this.activeSw = accountCreatedEvent.isActiveSw();
    }

    @CommandHandler
    public void handler(UpdateAccountCommand updateAccountCommand) {

        AccountUpdatedEvent accountUpdatedEvent = new AccountUpdatedEvent();
        BeanUtils.copyProperties(updateAccountCommand, accountUpdatedEvent);

        AggregateLifecycle.apply(accountUpdatedEvent);

    }

    @EventSourcingHandler
    public void on(AccountUpdatedEvent accountUpdatedEvent) {
        this.accountNumber = accountUpdatedEvent.getAccountNumber();
        this.activeSw = accountUpdatedEvent.isActiveSw();
    }

    @CommandHandler
    public void handler(DeleteAccountCommand deleteAccountCommand) {

        AccountDeletedEvent accountDeletedEvent = new AccountDeletedEvent();
        BeanUtils.copyProperties(deleteAccountCommand, accountDeletedEvent);

        AggregateLifecycle.apply(accountDeletedEvent);
    }

    @EventSourcingHandler
    public void on(AccountDeletedEvent accountDeletedEvent) {
        this.activeSw = accountDeletedEvent.isActiveSw();
    }

    @CommandHandler
    public void handler(UpdateAccountMobileNumberCommand updateAccountMobileNumberCommand) {
        log.info("Processing UpdateAccountMobileNumberCommand.\n\t[{}]",
                updateAccountMobileNumberCommand);

        // To create an event
        AccountMobileNumberUpdatedEvent accountMobileNumberUpdatedEvent =
                new AccountMobileNumberUpdatedEvent();

        // To copy all data properties
        BeanUtils.copyProperties(updateAccountMobileNumberCommand, accountMobileNumberUpdatedEvent);

        // To publish the event
        AggregateLifecycle.apply(accountMobileNumberUpdatedEvent);

        log.info("UpdateAccountMobileNumberCommand processed successfully.");
    }

    @EventSourcingHandler
    public void on(AccountMobileNumberUpdatedEvent accountMobileNumberUpdatedEvent) {

        // To set new value for mobile number which was updated
        this.mobileNumber = accountMobileNumberUpdatedEvent.getNewMobileNumber();
    }

    @CommandHandler
    public void handler(RollbackAccountMobileNumberCommand rollbackAccountMobileNumberCommand) {

        log.info("Processing RollbackAccountMobileNumberCommand.\n\t[{}]",
                rollbackAccountMobileNumberCommand);

        AccountMobileNumberRollbackedEvent accountMobileNumberRollbackedEvent =
                new AccountMobileNumberRollbackedEvent();

        BeanUtils.copyProperties(rollbackAccountMobileNumberCommand, accountMobileNumberRollbackedEvent);

        // To publish new event which is handled by AccountProjection.
        // However, SagaManager orchestrates this event in order to continue with the flow in reverse order to complete
        // the compensation transaction.
        AggregateLifecycle.apply(accountMobileNumberRollbackedEvent);

        log.info("RollbackAccountMobileNumberCommand processed successfully.");

    }

    @EventSourcingHandler
    public void on(AccountMobileNumberRollbackedEvent accountMobileNumberRollbackedEvent) {

        // To set new values for mobile number and error message according to update
        this.mobileNumber = accountMobileNumberRollbackedEvent.getCurrentMobileNumber();
        this.errorMessage = accountMobileNumberRollbackedEvent.getErrorMessage();
    }

}
