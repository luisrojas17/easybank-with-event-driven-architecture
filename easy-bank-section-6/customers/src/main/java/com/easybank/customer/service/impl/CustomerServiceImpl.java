package com.easybank.customer.service.impl;

import com.easybank.customer.command.event.CustomerCreatedEvent;
import com.easybank.customer.command.event.CustomerUpdatedEvent;
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
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public void create(CustomerCreatedEvent customerCreatedEvent) {

        Optional<CustomerEntity> optionalCustomer = customerRepository.findByMobileNumberAndActiveSw(
                customerCreatedEvent.getMobileNumber(), true);

        if (optionalCustomer.isPresent()) {
            throw new CustomerAlreadyExistsException("Customer already registered with given mobileNumber "
                    + customerCreatedEvent.getMobileNumber());
        }

        CustomerEntity customerEntity = new CustomerEntity();
        BeanUtils.copyProperties(customerCreatedEvent, customerEntity);

        customerRepository.save(customerEntity);

    }

    @Override
    public CustomerDto find(String mobileNumber) {
        CustomerEntity customer = customerRepository.findByMobileNumberAndActiveSw(mobileNumber, true).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );

        return CustomerMapper.mapToDto(customer, new CustomerDto());
    }

    @Override
    public boolean update(CustomerUpdatedEvent CustomerUpdatedEvent) {
        CustomerEntity customerEntity = customerRepository.findByMobileNumberAndActiveSw(CustomerUpdatedEvent.getMobileNumber(), true)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", CustomerUpdatedEvent.getMobileNumber()));

        // TODO : Make a mapper class
        customerEntity.setName(CustomerUpdatedEvent.getName());
        customerEntity.setEmail(CustomerUpdatedEvent.getEmail());

        customerRepository.save(customerEntity);

        return true;
    }

    @Override
    public boolean delete(String customerId) {
        CustomerEntity customer = customerRepository.findById(customerId).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "customerId", customerId)
        );
        customer.setActiveSw(CustomerConstants.IN_ACTIVE_SW);
        customerRepository.save(customer);
        return true;
    }

    @Override
    public boolean updateMobileNumber(String oldMobileNumber, String newMobileNumber) {

        log.info("Updating customer mobile number from [{}] to [{}].",
                oldMobileNumber, newMobileNumber);

        // It is searched the current mobile number before to update it.
        CustomerEntity customerEntity =
                customerRepository.findByMobileNumberAndActiveSw(oldMobileNumber, CustomerConstants.ACTIVE_SW)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Customer", "mobileNumber", oldMobileNumber));

        customerEntity.setMobileNumber(newMobileNumber);
        customerRepository.save(customerEntity);

        log.info("Customer mobile number was updated from [{}] to [{}] successfully.",
                oldMobileNumber, newMobileNumber);

        return true;
    }

}
