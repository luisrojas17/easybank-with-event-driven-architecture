package com.easybank.customer.command.aggregate;

import com.easybank.common.command.RollbackCustomerMobileNumberCommand;
import com.easybank.common.command.UpdateCustomerMobileNumberCommand;
import com.easybank.common.event.CustomerMobileNumberRollbackedEvent;
import com.easybank.common.event.CustomerMobileNumberUpdatedEvent;
import com.easybank.customer.command.CreateCustomerCommand;
import com.easybank.customer.command.DeleteCustomerCommand;
import com.easybank.customer.command.UpdateCustomerCommand;
import com.easybank.customer.command.event.CustomerCreatedEvent;
import com.easybank.customer.command.event.CustomerDeletedEvent;
import com.easybank.customer.command.event.CustomerUpdatedEvent;
import com.easybank.customer.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import java.util.Optional;

/**
 * This class is the aggregate class for customer. The aggregate encapsulates the state and behavior.
 * Through the aggregate it will be updated its values on the event store (Axon server).
 * On the other hand, it will be launched an event to synchronize the data in the reading database.
 *
 */
@Slf4j
@Aggregate
public class CustomerAggregate {

    // To say to axon server what it will be the primary key or id
    @AggregateIdentifier
    private String customerId;
    private String name;
    private String email;
    private String mobileNumber;
    private boolean activeSw;
    private String errorMessage;

    public CustomerAggregate() {}

    // To receive the commands created by the controller and publish the events to update
    // the data on the event store and to synchronize the data in the reading database.
    //
    // To know this constructor/method will be use when creating operation is invoked
    @CommandHandler
    public CustomerAggregate(CreateCustomerCommand createCustomerCommand, CustomerRepository customerRepository) {

        log.info("Processing CreateCustomerCommand.\n\t[{}]", createCustomerCommand);

        // To create an event
        CustomerCreatedEvent customerCreatedEvent = new CustomerCreatedEvent();

        // To copy all data properties
        BeanUtils.copyProperties(createCustomerCommand, customerCreatedEvent);

        // To publish the event
        AggregateLifecycle.apply(customerCreatedEvent);

        log.info("CreateCustomerCommand processed successfully.");

    }

    // To catch the event published to update the aggregate's values on event store.
    @EventSourcingHandler
    public void on(CustomerCreatedEvent customerCreatedEvent) {
        this.customerId = customerCreatedEvent.getCustomerId();
        this.name = customerCreatedEvent.getName();
        this.email = customerCreatedEvent.getEmail();
        this.mobileNumber = customerCreatedEvent.getMobileNumber();
        this.activeSw = customerCreatedEvent.isActiveSw();
    }

    @CommandHandler
    //public void handle(UpdateCustomerCommand updateCustomerCommand, EventStore eventStore) {
    public void handler(UpdateCustomerCommand updateCustomerCommand) {

        log.info("UpdateCustomerCommand.\n\t[{}]", updateCustomerCommand);

        /*List<?> commands =
            eventStore.readEvents(updateCustomerCommand.getCustomerId()).asStream().toList();

        if(commands.isEmpty()) {
            throw new ResourceNotFoundException("Customer", "customerId", updateCustomerCommand.getCustomerId());
        }*/

        // To create an event
        CustomerUpdatedEvent customerUpdatedEvent = new CustomerUpdatedEvent();

        // To copy all data properties
        BeanUtils.copyProperties(updateCustomerCommand, customerUpdatedEvent);

        // To publish the event
        AggregateLifecycle.apply(customerUpdatedEvent);

        log.info("UpdateCustomerCommand processed successfully.");
    }

    @EventSourcingHandler
    public void on(CustomerUpdatedEvent customerUpdatedEvent) {
        this.name = customerUpdatedEvent.getName();
        this.email = customerUpdatedEvent.getEmail();
    }

    // To receive the command to delete customer
    @CommandHandler
    public void handler(DeleteCustomerCommand deleteCustomerCommand) {

        log.info("Processing DeleteCustomerCommand.\n\t[{}]", deleteCustomerCommand);

        // To create an event
        CustomerDeletedEvent customerDeletedEvent = new CustomerDeletedEvent();

        // To copy all data properties
        BeanUtils.copyProperties(deleteCustomerCommand, customerDeletedEvent);

        // To publish the event
        AggregateLifecycle.apply(customerDeletedEvent);

        log.info("DeleteCustomerCommand processed successfully.");
    }

    @EventSourcingHandler
    public void on(CustomerDeletedEvent customerDeletedEvent) {
        this.activeSw = customerDeletedEvent.isActiveSw();
    }

    // To receive the command to update mobile number
    @CommandHandler
    public void handler(UpdateCustomerMobileNumberCommand updateCustomerMobileNumberCommand) {

        log.info("Processing UpdateCustomerMobileNumberCommand.\n\t[{}]",
                updateCustomerMobileNumberCommand);

        // To create an event
        CustomerMobileNumberUpdatedEvent customerMobileNumberUpdatedEvent =
                new CustomerMobileNumberUpdatedEvent();

        // To copy all data properties
        BeanUtils.copyProperties(updateCustomerMobileNumberCommand, customerMobileNumberUpdatedEvent);

        // To publish the event
        AggregateLifecycle.apply(customerMobileNumberUpdatedEvent);

        log.info("UpdateCustomerMobileNumberCommand processed successfully.");
    }

    @EventSourcingHandler
    public void on(CustomerMobileNumberUpdatedEvent customerMobileNumberUpdatedEvent) {

        // To set new value for mobile number which was updated
        this.mobileNumber = customerMobileNumberUpdatedEvent.getNewMobileNumber();
    }

    @CommandHandler
    public void handler(RollbackCustomerMobileNumberCommand rollbackCustomerMobileNumberCommand) {

        // To create an event
        CustomerMobileNumberRollbackedEvent customerMobileNumberRollbackedEvent =
                new CustomerMobileNumberRollbackedEvent();

        // To copy all data properties
        BeanUtils.copyProperties(rollbackCustomerMobileNumberCommand, customerMobileNumberRollbackedEvent);

        // To publish new event which is handled by CustomerProjection.
        // However, SagaManager orchestrates this event in order to continue with the flow in reverse order to complete
        // the compensation transaction.
        AggregateLifecycle.apply(customerMobileNumberRollbackedEvent);
    }

    @EventSourcingHandler
    public void on(CustomerMobileNumberRollbackedEvent customerMobileNumberRollbackedEvent) {

        // To set new values for mobile number and error message according to update
        this.mobileNumber = customerMobileNumberRollbackedEvent.getCurrentMobileNumber();
        this.errorMessage = customerMobileNumberRollbackedEvent.getErrorMessage();

    }
}
