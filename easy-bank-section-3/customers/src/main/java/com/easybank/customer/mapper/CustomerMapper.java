package com.easybank.customer.mapper;

import com.easybank.customer.dto.CustomerDto;
import com.easybank.customer.entity.CustomerEntity;

public class CustomerMapper {

    public static CustomerDto mapToDto(CustomerEntity customer, CustomerDto customerDto) {
        customerDto.setCustomerId(customer.getCustomerId());
        customerDto.setName(customer.getName());
        customerDto.setEmail(customer.getEmail());
        customerDto.setMobileNumber(customer.getMobileNumber());
        customerDto.setActiveSw(customer.isActiveSw());
        return customerDto;
    }

    public static CustomerEntity mapToCustomer(CustomerDto customerDto, CustomerEntity customer) {
        customer.setCustomerId(customerDto.getCustomerId());
        customer.setName(customerDto.getName());
        customer.setEmail(customerDto.getEmail());
        customer.setMobileNumber(customerDto.getMobileNumber());
        if(customerDto.isActiveSw()) {
            customer.setActiveSw(customerDto.isActiveSw());
        }
        return customer;
    }

}
