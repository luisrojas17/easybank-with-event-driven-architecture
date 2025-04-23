package com.easybank.customer.command.aggregate;

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
 * They produce and process events
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

    public CustomerAggregate() {}

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

    // To catch the event published
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
}
