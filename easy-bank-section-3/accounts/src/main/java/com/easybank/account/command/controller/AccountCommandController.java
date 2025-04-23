package com.easybank.account.command.controller;

import com.easybank.account.command.CreateAccountCommand;
import com.easybank.account.command.DeleteAccountCommand;
import com.easybank.account.command.UpdateAccountCommand;
import com.easybank.account.constants.AccountsConstants;
import com.easybank.account.dto.AccountDto;
import com.easybank.account.dto.ResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
@RequiredArgsConstructor
public class AccountCommandController {

    private final CommandGateway commandGateway;

    @PostMapping("/create")
    public ResponseEntity<ResponseDto> create(
            @RequestParam("mobileNumber")
            @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits") String mobileNumber) {

        //long randomAccountNumber = (long) Math.floor(Math.random() * 1000000000L);
        long randomAccountNumber = 1000000000L + new Random().nextInt(900000000);
        CreateAccountCommand createAccountCommand = CreateAccountCommand.builder()
                .accountNumber(randomAccountNumber)
                .mobileNumber(mobileNumber)
                .accountType(AccountsConstants.SAVINGS)
                .branchAddress(AccountsConstants.ADDRESS)
                .activeSw(AccountsConstants.ACTIVE_SW).build();

        commandGateway.sendAndWait(createAccountCommand);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(AccountsConstants.STATUS_201, AccountsConstants.MESSAGE_201));
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDto> update(
            @Valid @RequestBody AccountDto accountDto) {

        UpdateAccountCommand updateAccountCommand = UpdateAccountCommand.builder()
                .accountNumber(accountDto.getAccountNumber())
                .mobileNumber(accountDto.getMobileNumber())
                .accountType(accountDto.getAccountType())
                .branchAddress(accountDto.getBranchAddress())
                .activeSw(AccountsConstants.ACTIVE_SW).build();

        commandGateway.sendAndWait(updateAccountCommand);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));

    }

    @PatchMapping("/delete")
    public ResponseEntity<ResponseDto> delete(
            @RequestParam("accountNumber") Long accountNumber) {

        DeleteAccountCommand deleteAccountCommand = DeleteAccountCommand.builder()
                .accountNumber(accountNumber)
                .activeSw(AccountsConstants.IN_ACTIVE_SW).build();

        commandGateway.sendAndWait(deleteAccountCommand);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
    }
}
