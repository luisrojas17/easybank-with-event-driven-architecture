package com.easybank.customer.service.impl;

import com.easybank.common.event.CustomerDataChangedEvent;
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
import lombok.RequiredArgsConstructor;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final EventGateway eventGateway;

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
        CustomerEntity customerEntity = customerRepository.findById(customerId).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "customerId", customerId)
        );
        customerEntity.setActiveSw(CustomerConstants.IN_ACTIVE_SW);
        customerRepository.save(customerEntity);

        // To publish the data changed event when customer is deleted (logically)
        // and the changes can be shown their by profile microservice.
        CustomerDataChangedEvent customerDataChangedEvent =
                CustomerDataChangedEvent.builder()
                        .name(customerEntity.getName())
                        .mobileNumber(customerEntity.getMobileNumber())
                        .activeSw(CustomerConstants.IN_ACTIVE_SW)
                        .build();

        eventGateway.publish(customerDataChangedEvent);

        return true;
    }

}
