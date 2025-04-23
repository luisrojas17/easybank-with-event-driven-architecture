package com.easybank.account.query.controller;

import com.easybank.account.dto.AccountDto;
import com.easybank.account.query.FindAccountQuery;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
@RequiredArgsConstructor
public class AccountQueryController {

    private final QueryGateway queryGateway;

    @GetMapping("/fetch")
    public ResponseEntity<AccountDto> fetch(@RequestParam("mobileNumber")
                                             @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
                                             String mobileNumber) {

        FindAccountQuery findAccountQuery = new FindAccountQuery(mobileNumber);

        AccountDto accountDto = queryGateway.query(findAccountQuery,
                ResponseTypes.instanceOf(AccountDto.class)).join();

        return ResponseEntity.status(HttpStatus.OK).body(accountDto);

    }
}
