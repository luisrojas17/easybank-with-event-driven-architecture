package com.easybank.customer.controller;

import com.easybank.common.dto.MobileNumberToUpdateDto;
import com.easybank.customer.constants.CustomerConstants;
import com.easybank.customer.dto.CustomerDto;
import com.easybank.customer.dto.ResponseDto;
import com.easybank.customer.service.CustomerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseDto> create(@Valid @RequestBody CustomerDto customerDto) {
        customerDto.setCustomerId(UUID.randomUUID().toString());
        customerService.create(customerDto);
        return ResponseEntity
                .status(org.springframework.http.HttpStatus.CREATED)
                .body(new ResponseDto(CustomerConstants.STATUS_201, CustomerConstants.MESSAGE_201));
    }

    @GetMapping("/fetch")
    public ResponseEntity<CustomerDto> fetch(@RequestParam("mobileNumber")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
    String mobileNumber) {
        CustomerDto fetchedCustomer = customerService.fetch(mobileNumber);
        return ResponseEntity.status(org.springframework.http.HttpStatus.OK).body(fetchedCustomer);
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDto> update(@Valid @RequestBody CustomerDto customerDto) {
        boolean isUpdated = customerService.update(customerDto);
        if (isUpdated) {
            return ResponseEntity
                    .status(org.springframework.http.HttpStatus.OK)
                    .body(new ResponseDto(CustomerConstants.STATUS_200, CustomerConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDto(CustomerConstants.STATUS_500,
                            CustomerConstants.MESSAGE_500_UPDATE));
        }
    }

    @PatchMapping("/delete")
    public ResponseEntity<ResponseDto> delete(@RequestParam("customerId")
    @Pattern(regexp = "(^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$)",
            message = "CustomerId is invalid") String customerId) {
        boolean isDeleted = customerService.delete(customerId);
        if (isDeleted) {
            return ResponseEntity
                    .status(org.springframework.http.HttpStatus.OK)
                    .body(new ResponseDto(CustomerConstants.STATUS_200,
                            CustomerConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDto(CustomerConstants.STATUS_500,
                            CustomerConstants.MESSAGE_500_DELETE));
        }
    }

    @PatchMapping("mobile-number")
    public ResponseEntity<ResponseDto> updateMobileNumber(@Valid @RequestBody MobileNumberToUpdateDto mobileNumberToUpdateDto) {

        customerService.updateMobileNumber(mobileNumberToUpdateDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDto(
                        CustomerConstants.STATUS_200, CustomerConstants.MESSAGE_200));
    }

}
