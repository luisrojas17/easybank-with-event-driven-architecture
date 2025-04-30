package com.easybank.account.query.projection;

import com.easybank.account.command.event.AccountCreatedEvent;
import com.easybank.account.command.event.AccountDeletedEvent;
import com.easybank.account.command.event.AccountUpdatedEvent;
import com.easybank.account.dto.AccountDto;
import com.easybank.account.service.AccountService;
import com.easybank.common.event.AccountMobileNumberRollbackedEvent;
import com.easybank.common.event.AccountMobileNumberUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * This class contains methods to process the events published by the aggregate component.
 * Each method is annotated with @EventHandler in order to listen to the events published by the aggregate component.
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ProcessingGroup("account-group")
public class AccountProjection {

    private final AccountService accountService;

    @EventHandler
    public void handler(AccountCreatedEvent accountCreatedEvent) {

        log.info("Processing AccountCreatedEvent.\n\t[{}]", accountCreatedEvent);

        AccountDto accountDto = new AccountDto();

        BeanUtils.copyProperties(accountCreatedEvent, accountDto);

        accountService.create(accountDto);

        log.info("AccountCreatedEvent processed successfully.");
    }

    @EventHandler
    public void handler(AccountUpdatedEvent accountUpdatedEvent) {

        log.info("Processing AccountUpdatedEvent.\n\t[{}]", accountUpdatedEvent);

        AccountDto accountDto = new AccountDto();

        BeanUtils.copyProperties(accountUpdatedEvent, accountDto);

        boolean result = accountService.update(accountDto);

        log.info("AccountUpdatedEvent processed successfully [{}].", result);
    }

    @EventHandler
    public void handler(AccountDeletedEvent accountDeletedEvent) {

        log.info("Processing AccountDeletedEvent.\n\t[{}]", accountDeletedEvent.getAccountNumber());

        boolean result = accountService.delete(accountDeletedEvent.getAccountNumber());

        log.info("AccountDeletedEvent processed successfully [{}].", result);
    }

    // To handle the event published by the aggregate (AccountAggregate) in order to update mobile number
    @EventHandler
    public void handler(AccountMobileNumberUpdatedEvent accountMobileNumberUpdatedEvent) {

        log.info("Processing AccountMobileNumberUpdatedEvent.\n\t[{}]",
                accountMobileNumberUpdatedEvent.getCustomerId());

        accountService.updateMobileNumber(
                accountMobileNumberUpdatedEvent.getCurrentMobileNumber(), accountMobileNumberUpdatedEvent.getNewMobileNumber());

        log.info("AccountMobileNumberUpdatedEvent processed successfully.");
    }

    // To handle the event published by the aggregate (AccountAggregate) in order to make rollback
    @EventHandler
    public void handler(AccountMobileNumberRollbackedEvent accountMobileNumberRollbackedEvent) {
        log.info("Processing AccountMobileNumberRollbackedEvent.\n\t[{}]",
                accountMobileNumberRollbackedEvent.getCustomerId());

        accountService.updateMobileNumber(
                accountMobileNumberRollbackedEvent.getNewMobileNumber(), accountMobileNumberRollbackedEvent.getCurrentMobileNumber());

        log.info("AccountMobileNumberRollbackedEvent processed successfully.");
    }
}
