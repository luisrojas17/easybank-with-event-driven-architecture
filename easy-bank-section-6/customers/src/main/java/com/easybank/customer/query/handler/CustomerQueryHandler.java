package com.easybank.customer.query.handler;

import com.easybank.customer.dto.CustomerDto;
import com.easybank.customer.query.FindCustomerQuery;
import com.easybank.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerQueryHandler {

    private final CustomerService customerService;

    // This method is invoking through QueryGateway instance which is injected by Controller.
    @QueryHandler
    public CustomerDto findCustomer(FindCustomerQuery findCustomerQuery) {

        log.info("Processing FindCustomerQuery.\n[{}]", findCustomerQuery);

        return customerService.find(findCustomerQuery.getMobileNumber());
    }
}
