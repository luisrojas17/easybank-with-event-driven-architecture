package com.easybank.customer.mapper;

import com.easybank.customer.dto.CustomerDto;
import com.easybank.customer.entity.CustomerEntity;

public class CustomerMapper {

    public static CustomerDto mapToDto(CustomerEntity customerEntity, CustomerDto customerDto) {
        customerDto.setCustomerId(customerEntity.getCustomerId());
        customerDto.setName(customerEntity.getName());
        customerDto.setEmail(customerEntity.getEmail());
        customerDto.setMobileNumber(customerEntity.getMobileNumber());
        customerDto.setActiveSw(customerEntity.isActiveSw());
        return customerDto;
    }

    public static CustomerEntity mapToEntity(CustomerDto customerDto, CustomerEntity customerEntity) {
        customerEntity.setCustomerId(customerDto.getCustomerId());
        customerEntity.setName(customerDto.getName());
        customerEntity.setEmail(customerDto.getEmail());
        customerEntity.setMobileNumber(customerDto.getMobileNumber());
        if(customerDto.isActiveSw()) {
            customerEntity.setActiveSw(customerDto.isActiveSw());
        }
        return customerEntity;
    }

}
