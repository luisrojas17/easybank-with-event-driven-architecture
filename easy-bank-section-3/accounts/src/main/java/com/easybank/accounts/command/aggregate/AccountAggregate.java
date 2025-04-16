package com.easybank.accounts.command.aggregate;

import com.easybank.accounts.command.CreateAccountCommand;
import com.easybank.accounts.command.DeleteAccountCommand;
import com.easybank.accounts.command.UpdateAccountCommand;
import com.easybank.accounts.command.event.AccountCreatedEvent;
import com.easybank.accounts.command.event.AccountDeletedEvent;
import com.easybank.accounts.command.event.AccountUpdatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

@Aggregate
public class AccountAggregate {

    @AggregateIdentifier
    private Long accountNumber;
    private String mobileNumber;
    private String accountType;
    private String branchAddress;
    private boolean activeSw;
    private String errorMsg;

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

}
