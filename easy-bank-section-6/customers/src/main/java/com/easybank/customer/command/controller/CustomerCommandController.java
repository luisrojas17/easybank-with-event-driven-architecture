package com.easybank.customer.command.controller;

import com.easybank.common.command.UpdateCustomerMobileNumberCommand;
import com.easybank.common.dto.MobileNumberToUpdateDto;
import com.easybank.customer.command.CreateCustomerCommand;
import com.easybank.customer.command.DeleteCustomerCommand;
import com.easybank.customer.command.UpdateCustomerCommand;
import com.easybank.customer.constants.CustomerConstants;
import com.easybank.customer.dto.CustomerDto;
import com.easybank.customer.dto.ResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
@RequiredArgsConstructor
public class CustomerCommandController {

    // To send commands to axon server
    private final CommandGateway commandGateway;

    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createCustomer(@Valid @RequestBody CustomerDto customerDto) {

        CreateCustomerCommand createCustomerCommand = CreateCustomerCommand.builder()
                .customerId(UUID.randomUUID().toString())
                .name(customerDto.getName())
                .email(customerDto.getEmail())
                .mobileNumber(customerDto.getMobileNumber())
                .activeSw(CustomerConstants.ACTIVE_SW)
                .build();

        commandGateway.sendAndWait(createCustomerCommand);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(CustomerConstants.STATUS_201, CustomerConstants.MESSAGE_201));
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateCustomerDetails(@Valid @RequestBody CustomerDto customerDto) {

        UpdateCustomerCommand updateCustomerCommand = UpdateCustomerCommand.builder()
                .customerId(customerDto.getCustomerId())
                .name(customerDto.getName())
                .email(customerDto.getEmail())
                .mobileNumber(customerDto.getMobileNumber())
                .activeSw(CustomerConstants.ACTIVE_SW)
                .build();

        commandGateway.sendAndWait(updateCustomerCommand);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDto(CustomerConstants.STATUS_200, CustomerConstants.MESSAGE_200));
    }

    @PatchMapping("/delete")
    public ResponseEntity<ResponseDto> deleteCustomer(
            @RequestParam("customerId")
            @Pattern(regexp = "(^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$)",
                                                              message = "CustomerId is invalid") String customerId) {

        DeleteCustomerCommand deleteCustomerCommand = DeleteCustomerCommand.builder()
                .customerId(customerId)
                .activeSw(CustomerConstants.IN_ACTIVE_SW)
                .build();

        commandGateway.sendAndWait(deleteCustomerCommand);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDto(CustomerConstants.STATUS_200, CustomerConstants.MESSAGE_200));
    }

    @PatchMapping("/mobile-number")
    public ResponseEntity<ResponseDto> updateMobileNumber(
            @Valid @RequestBody MobileNumberToUpdateDto
                    mobileNumberToUpdateDto) {

        UpdateCustomerMobileNumberCommand updateCustomerMobileNumberCommand =
                UpdateCustomerMobileNumberCommand.builder()
                        .customerId(mobileNumberToUpdateDto.getCustomerId())
                        .accountNumber(mobileNumberToUpdateDto.getAccountNumber())
                        .cardNumber(mobileNumberToUpdateDto.getCardNumber())
                        .loanNumber(mobileNumberToUpdateDto.getLoanNumber())
                        .currentMobileNumber(mobileNumberToUpdateDto.getCurrentMobileNumber())
                        .newMobileNumber(mobileNumberToUpdateDto.getNewMobileNumber())
                        .build();

        // To send command to update mobile number
        commandGateway.sendAndWait(updateCustomerMobileNumberCommand);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDto(CustomerConstants.STATUS_200, CustomerConstants.MESSAGE_200));
    }

}
