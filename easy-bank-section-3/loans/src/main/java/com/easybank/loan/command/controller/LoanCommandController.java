package com.easybank.loan.command.controller;

import com.easybank.loan.command.CreateLoanCommand;
import com.easybank.loan.command.DeleteLoanCommand;
import com.easybank.loan.command.UpdateLoanCommand;
import com.easybank.loan.constants.LoanConstants;
import com.easybank.loan.dto.LoanDto;
import com.easybank.loan.dto.ResponseDto;
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
public class LoanCommandController {

    private final CommandGateway commandGateway;

    @PostMapping("/create")
    public ResponseEntity<ResponseDto> create(@RequestParam("mobileNumber")
        @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits") String mobileNumber) {

        long randomLoanNumber = 1000000000L + new Random().nextInt(900000000);
        CreateLoanCommand createLoanCommand = CreateLoanCommand.builder()
                .loanNumber(randomLoanNumber)
                .mobileNumber(mobileNumber)
                .loanType(LoanConstants.HOME_LOAN)
                .totalLoan(LoanConstants.NEW_LOAN_LIMIT)
                .amountPaid(0)
                .outstandingAmount(LoanConstants.NEW_LOAN_LIMIT)
                .activeSw(LoanConstants.ACTIVE_SW)
                .build();

        commandGateway.sendAndWait(createLoanCommand);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(LoanConstants.STATUS_201, LoanConstants.MESSAGE_201));
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDto> update(@Valid @RequestBody LoanDto dto) {
        UpdateLoanCommand updateLoanCommand = UpdateLoanCommand.builder()
                .loanNumber(dto.getLoanNumber())
                .mobileNumber(dto.getMobileNumber())
                .loanType(LoanConstants.HOME_LOAN)
                .totalLoan(dto.getTotalLoan())
                .amountPaid(dto.getAmountPaid())
                .outstandingAmount(dto.getOutstandingAmount())
                .activeSw(LoanConstants.ACTIVE_SW)
                .build();

        commandGateway.sendAndWait(updateLoanCommand);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(LoanConstants.STATUS_200, LoanConstants.MESSAGE_200));
    }

    @PatchMapping("/delete")
    public ResponseEntity<ResponseDto> delete(@RequestParam("loanNumber") Long loanNumber) {
        DeleteLoanCommand deleteLoanCommand = DeleteLoanCommand.builder()
                .loanNumber(loanNumber)
                .activeSw(LoanConstants.IN_ACTIVE_SW)
                .build();

        commandGateway.sendAndWait(deleteLoanCommand);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(LoanConstants.STATUS_200, LoanConstants.MESSAGE_200));

    }
}
