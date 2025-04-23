package com.easybank.customer.service;

import com.easybank.customer.command.event.CustomerCreatedEvent;
import com.easybank.customer.command.event.CustomerUpdatedEvent;
import com.easybank.customer.dto.CustomerDto;

public interface CustomerService {

    /**
     * @param customerCreatedEvent - CustomerCreatedEvent Object
     */
    void create(CustomerCreatedEvent customerCreatedEvent);

    /**
     * @param mobileNumber - Input Mobile Number
     * @return Accounts Details based on a given mobileNumber
     */
    CustomerDto find(String mobileNumber);

    /**
     * @param customerUpdatedEvent - CustomerUpdatedEvent Object
     * @return boolean indicating if the update of Account details is successful or not
     */
    boolean update(CustomerUpdatedEvent customerUpdatedEvent);

    /**
     * @param customerId - Input Customer ID
     * @return boolean indicating if the delete of Customer details is successful or not
     */
    boolean delete(String customerId);
}
