package com.easybank.accounts.query.handler;

import com.easybank.accounts.dto.AccountDto;
import com.easybank.accounts.query.FindAccountQuery;
import com.easybank.accounts.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryHandler;
import org.checkerframework.checker.index.qual.SameLen;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountQueryHandler {

    private final AccountService accountService;

    // This method is invoking through QueryGateway instance which is injected by Controller.
    @QueryHandler
    public AccountDto findAccount(FindAccountQuery findAccountQuery) {

        log.info("Processing FindAccountQuery.\n[{}]", findAccountQuery);

        AccountDto accountDto = accountService.fetch(findAccountQuery.getMobileNumber());

        log.info("Account details were gotten for mobileNumber [{}] are [{}]",
                findAccountQuery.getMobileNumber(), accountDto);

        return accountDto;
    }

}
