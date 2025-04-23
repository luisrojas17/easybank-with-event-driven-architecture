package com.easybank.loan.command.aggregate;

import com.easybank.loan.command.CreateLoanCommand;
import com.easybank.loan.command.DeleteLoanCommand;
import com.easybank.loan.command.UpdateLoanCommand;
import com.easybank.loan.command.event.LoanCreatedEvent;
import com.easybank.loan.command.event.LoanDeletedEvent;
import com.easybank.loan.command.event.LoanUpdatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

@Aggregate
public class LoanAggregate {

    @AggregateIdentifier
    private Long loanNumber;
    private String mobileNumber;
    private String loanType;
    private String loanStatus;
    private int totalLoan;
    private int amountPaid;
    private int outstandingAmount;
    private boolean activeSw;

    private final String LOAN_APPROVAL_DEADLINE = "loan-approval-deadline-";

    public LoanAggregate() {}

    @CommandHandler
    public LoanAggregate(CreateLoanCommand createLoanCommand) {
        LoanCreatedEvent loanCreatedEvent = new LoanCreatedEvent();
        BeanUtils.copyProperties(createLoanCommand, loanCreatedEvent);
        AggregateLifecycle.apply(loanCreatedEvent);
    }

    @EventSourcingHandler
    public void on(LoanCreatedEvent loanCreatedEvent) {
        this.loanNumber = loanCreatedEvent.getLoanNumber();
        this.mobileNumber = loanCreatedEvent.getMobileNumber();
        this.loanType = loanCreatedEvent.getLoanType();
        this.loanStatus = loanCreatedEvent.getLoanStatus();
        this.totalLoan = loanCreatedEvent.getTotalLoan();
        this.amountPaid = loanCreatedEvent.getAmountPaid();
        this.outstandingAmount = loanCreatedEvent.getOutstandingAmount();
        this.activeSw = loanCreatedEvent.isActiveSw();
    }

    @CommandHandler
    public void handler(UpdateLoanCommand updateLoanCommand) {
        LoanUpdatedEvent loanUpdatedEvent = new LoanUpdatedEvent();
        BeanUtils.copyProperties(updateLoanCommand, loanUpdatedEvent);
        AggregateLifecycle.apply(loanUpdatedEvent);
    }

    @EventSourcingHandler
    public void on(LoanUpdatedEvent loanUpdatedEvent) {
        this.loanType = loanUpdatedEvent.getLoanType();
        this.totalLoan = loanUpdatedEvent.getTotalLoan();
        this.amountPaid = loanUpdatedEvent.getAmountPaid();
        this.outstandingAmount = loanUpdatedEvent.getOutstandingAmount();
    }

    @CommandHandler
    public void handler(DeleteLoanCommand deleteLoanCommand) {
        LoanDeletedEvent loanDeletedEvent = new LoanDeletedEvent();
        BeanUtils.copyProperties(deleteLoanCommand, loanDeletedEvent);
        AggregateLifecycle.apply(loanDeletedEvent);
    }

    @EventSourcingHandler
    public void on(LoanDeletedEvent loanDeletedEvent) {
        this.activeSw = loanDeletedEvent.isActiveSw();
    }

}
