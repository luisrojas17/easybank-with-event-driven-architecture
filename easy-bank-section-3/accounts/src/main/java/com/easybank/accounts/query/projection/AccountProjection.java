package com.easybank.accounts.query.projection;

import com.easybank.accounts.command.event.AccountCreatedEvent;
import com.easybank.accounts.command.event.AccountDeletedEvent;
import com.easybank.accounts.command.event.AccountUpdatedEvent;
import com.easybank.accounts.dto.AccountDto;
import com.easybank.accounts.service.AccountService;
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
}
