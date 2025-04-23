package com.easybank.profile.query.projection;

import com.easybank.common.event.AccountDataChangedEvent;
import com.easybank.common.event.CardDataChangedEvent;
import com.easybank.common.event.CustomerDataChangedEvent;
import com.easybank.common.event.LoanDataChangedEvent;
import com.easybank.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

/**
 * This class contains methods to process the events published by the aggregate component in
 * Microservices Accounts, Cards, Customers and Loans.
 * Each method is annotated with @EventHandler in order to listen to the events published
 * by the aggregate component.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ProfileProjection {

    private final ProfileService profileService;

    // To receive the event and store customer data updated into the reading database
    @EventHandler
    public void handler(AccountDataChangedEvent accountDataChangedEvent) {

        log.info("Processing AccountDataChangedEvent: [{}].", accountDataChangedEvent);

        profileService.handler(accountDataChangedEvent);

        log.info("AccountDataChangedEvent processed successfully.");
    }

    @EventHandler
    public void handler(CardDataChangedEvent cardDataChangedEvent) {

        log.info("Processing CardDataChangedEvent: [{}].", cardDataChangedEvent);

        profileService.handler(cardDataChangedEvent);

        log.info("CardDataChangedEvent processed successfully.");
    }

    @EventHandler
    public void handler(CustomerDataChangedEvent customerDataChangedEvent) {

        log.info("Processing CustomerDataChangedEvent: [{}].", customerDataChangedEvent);

        profileService.handler(customerDataChangedEvent);

        log.info("CustomerDataChangedEvent processed successfully.");
    }

    @EventHandler
    public void handler(LoanDataChangedEvent loanDataChangedEvent) {

        log.info("Processing LoanDataChangedEvent: [{}].", loanDataChangedEvent);

        profileService.handler(loanDataChangedEvent);

        log.info("LoanDataChangedEvent processed successfully.");
    }

}
