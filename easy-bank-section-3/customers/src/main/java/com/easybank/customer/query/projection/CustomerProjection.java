package com.easybank.customer.query.projection;

import com.easybank.customer.command.event.CustomerCreatedEvent;
import com.easybank.customer.command.event.CustomerDeletedEvent;
import com.easybank.customer.command.event.CustomerUpdatedEvent;
import com.easybank.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

/**
 * This class contains methods to process the events published by the aggregate component.
 * Each method is annotated with @EventHandler in order to listen to the events published by the aggregate component.
 */
@Slf4j
@Component
@ProcessingGroup("customer-group")
@RequiredArgsConstructor
public class CustomerProjection {

    private final CustomerService customerService;

    // To receive the event and store customer data updated into the reading database
    @EventHandler
    public void on(CustomerCreatedEvent customerCreatedEvent) {

        log.info("Processing CustomerCreatedEvent.\n\t[{}]", customerCreatedEvent);

        customerService.create(customerCreatedEvent);

        log.info("CustomerCreatedEvent processed successfully. \n\t[{}]",
                customerCreatedEvent.getCustomerId());
    }

    @EventHandler
    public void on(CustomerUpdatedEvent customerUpdatedEvent) {

        log.info("Processing CustomerUpdatedEvent.\n\t[{}]",
                customerUpdatedEvent.getCustomerId());

        customerService.update(customerUpdatedEvent);

        log.info("CustomerUpdatedEvent processed successfully.");

    }

    @EventHandler
    public void on(CustomerDeletedEvent customerDeletedEvent) {

        log.info("Processing CustomerDeletedEvent.\n\t[{}]",
                customerDeletedEvent.getCustomerId());

        customerService.delete(customerDeletedEvent.getCustomerId());

        log.info("CustomerDeletedEvent processed successfully.");

    }
}
