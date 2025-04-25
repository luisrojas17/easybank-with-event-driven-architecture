package com.easybank.customer.service.impl;

import com.easybank.customer.constants.CustomerConstants;
import com.easybank.customer.dto.CustomerDto;
import com.easybank.customer.entity.CustomerEntity;
import com.easybank.customer.exception.CustomerAlreadyExistsException;
import com.easybank.customer.exception.ResourceNotFoundException;
import com.easybank.customer.mapper.CustomerMapper;
import com.easybank.customer.repository.CustomerRepository;
import com.easybank.customer.service.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private CustomerRepository customerRepository;

    @Override
    public void createCustomer(CustomerDto customerDto) {
        customerDto.setActiveSw(CustomerConstants.ACTIVE_SW);
        CustomerEntity customerEntity = CustomerMapper.mapToEntity(customerDto, new CustomerEntity());
        Optional<CustomerEntity> optionalCustomer = customerRepository.findByMobileNumberAndActiveSw(
                customerDto.getMobileNumber(), true);
        if (optionalCustomer.isPresent()) {
            throw new CustomerAlreadyExistsException("Customer already registered with given mobileNumber "
                    + customerDto.getMobileNumber());
        }
        CustomerEntity savedCustomerEntity = customerRepository.save(customerEntity);
    }

    @Override
    public CustomerDto fetchCustomer(String mobileNumber) {
        CustomerEntity customerEntity = customerRepository.findByMobileNumberAndActiveSw(mobileNumber, true).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        CustomerDto customerDto = CustomerMapper.mapToDto(customerEntity, new CustomerDto());

        log.info("Customer details for mobileNumber [{}], [{}]",
                mobileNumber, customerDto);

        return customerDto;
    }

    @Override
    public boolean updateCustomer(CustomerDto customerDto) {
        CustomerEntity customerEntity = customerRepository.findByMobileNumberAndActiveSw(customerDto.getMobileNumber(), true)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", customerDto.getMobileNumber()));
        CustomerMapper.mapToEntity(customerDto, customerEntity);
        customerRepository.save(customerEntity);
        return true;
    }

    @Override
    public boolean deleteCustomer(String customerId) {
        CustomerEntity customerEntity = customerRepository.findById(customerId).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "customerId", customerId)
        );
        customerEntity.setActiveSw(CustomerConstants.IN_ACTIVE_SW);
        customerRepository.save(customerEntity);
        return true;
    }

}
