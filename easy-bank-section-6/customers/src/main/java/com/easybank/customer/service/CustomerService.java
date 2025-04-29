package com.easybank.customer.service;

import com.easybank.customer.command.event.CustomerCreatedEvent;
import com.easybank.customer.command.event.CustomerUpdatedEvent;
import com.easybank.customer.dto.CustomerDto;

public interface CustomerService {

    void create(CustomerCreatedEvent customerCreatedEvent);

    CustomerDto find(String mobileNumber);

    boolean update(CustomerUpdatedEvent customerUpdatedEvent);

    boolean delete(String customerId);

    boolean updateMobileNumber(String oldMobileNumber, String newMobileNumber);

}
