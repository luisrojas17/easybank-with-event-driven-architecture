package com.easybank.customer.service;

import com.easybank.common.dto.MobileNumberToUpdateDto;
import com.easybank.customer.dto.CustomerDto;

public interface CustomerService {

    void create(CustomerDto customerDto);

    CustomerDto fetch(String mobileNumber);

    boolean update(CustomerDto customerDto);

    boolean delete(String customerId);

    boolean updateMobileNumber(MobileNumberToUpdateDto mobileNumberToUpdateDto);

    boolean rollbackMobileNumber(MobileNumberToUpdateDto mobileNumberToUpdateDto);
}
