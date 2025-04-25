package com.easybank.customer.service.impl;

import com.easybank.common.dto.MobileNumberToUpdateDto;
import com.easybank.customer.constants.CustomerConstants;
import com.easybank.customer.dto.CustomerDto;
import com.easybank.customer.entity.CustomerEntity;
import com.easybank.customer.exception.CustomerAlreadyExistsException;
import com.easybank.customer.exception.ResourceNotFoundException;
import com.easybank.customer.mapper.CustomerMapper;
import com.easybank.customer.repository.CustomerRepository;
import com.easybank.customer.service.CustomerService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final StreamBridge streamBridge;

    @Override
    public void create(CustomerDto customerDto) {
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
    public CustomerDto fetch(String mobileNumber) {
        CustomerEntity customerEntity = customerRepository.findByMobileNumberAndActiveSw(mobileNumber, true).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        CustomerDto customerDto = CustomerMapper.mapToDto(customerEntity, new CustomerDto());

        log.info("Customer details for mobileNumber [{}], [{}]",
                mobileNumber, customerDto);

        return customerDto;
    }

    @Override
    public boolean update(CustomerDto customerDto) {
        CustomerEntity customerEntity = customerRepository.findByMobileNumberAndActiveSw(customerDto.getMobileNumber(), true)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", customerDto.getMobileNumber()));
        CustomerMapper.mapToEntity(customerDto, customerEntity);
        customerRepository.save(customerEntity);

        return true;
    }

    @Override
    public boolean delete(String customerId) {
        CustomerEntity customerEntity = customerRepository.findById(customerId).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "customerId", customerId)
        );
        customerEntity.setActiveSw(CustomerConstants.IN_ACTIVE_SW);
        customerRepository.save(customerEntity);

        return true;
    }

    @Override
    public boolean updateMobileNumber(MobileNumberToUpdateDto mobileNumberToUpdateDto) {

        log.info("Updating mobile number [{}].",
                mobileNumberToUpdateDto.getCurrentMobileNumber());

        CustomerEntity customerEntity =
                customerRepository.findByMobileNumberAndActiveSw(
                        mobileNumberToUpdateDto.getCurrentMobileNumber(), true)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Customer", "mobileNumber",
                                        mobileNumberToUpdateDto.getCurrentMobileNumber()));

        customerEntity.setMobileNumber(mobileNumberToUpdateDto.getNewMobileNumber());

        customerRepository.save(customerEntity);

        log.info("Mobile number [{}] was updated to [{}] successfully.",
                mobileNumberToUpdateDto.getCurrentMobileNumber(), mobileNumberToUpdateDto.getNewMobileNumber());

        publishEvent(mobileNumberToUpdateDto);

        return true;
    }

    /**
     * This method publish an event in to Account Queue in order to update the mobile number
     * for Account Microservice and keeping the data consistency through all Microservices' Databases.
     *
     * @param mobileNumberToUpdateDto an instance of MobileNumberToUpdateDto which contains current and new
     *                                mobile number to be updated.
     */
    private void publishEvent(MobileNumberToUpdateDto mobileNumberToUpdateDto) {

        log.info("Publishing event to update mobile number [{}].",
                mobileNumberToUpdateDto);

        // The first parameter must be to same defined in application.yml -> cloud.stream.bindings
        var result =
                streamBridge.send("updateAccountMobileNumber-out-0", mobileNumberToUpdateDto);

        log.info("Event published successfully [{}].", result);
    }

}
