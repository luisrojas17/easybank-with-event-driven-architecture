package com.easybank.gatewayserver.handler;

import com.easybank.gatewayserver.dto.*;
import com.easybank.gatewayserver.service.client.CustomerSummaryClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CustomerCompositeHandler {

    private final CustomerSummaryClient customerSummaryClient;

    public Mono<ServerResponse> fetchCustomerSummary(ServerRequest serverRequest) {
        String mobileNumber = serverRequest.queryParam("mobileNumber").get();

        Mono<ResponseEntity<CustomerDto>> customerDetails = customerSummaryClient.fetchCustomerDetails(mobileNumber);
        Mono<ResponseEntity<AccountDto>> accountDetails = customerSummaryClient.fetchAccountDetails(mobileNumber);
        Mono<ResponseEntity<LoanDto>> loanDetails = customerSummaryClient.fetchLoanDetails(mobileNumber);
        Mono<ResponseEntity<CardDto>> cardDetails = customerSummaryClient.fetchCardDetails(mobileNumber);

        return Mono.zip(customerDetails, accountDetails, loanDetails, cardDetails)
                .flatMap(tuple -> {
                    CustomerDto customerDto = tuple.getT1().getBody();
                    AccountDto accountDto = tuple.getT2().getBody();
                    LoanDto loanDto = tuple.getT3().getBody();
                    CardDto cardDto = tuple.getT4().getBody();
                    CustomerSummaryDto customerSummaryDto = new CustomerSummaryDto(customerDto, accountDto, loanDto, cardDto);
                    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                            .body(BodyInserters.fromValue(customerSummaryDto));
                });


    }

}
