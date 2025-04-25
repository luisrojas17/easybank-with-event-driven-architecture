package com.easybank.account.service.impl;

import com.easybank.account.constants.AccountsConstants;
import com.easybank.account.dto.AccountDto;
import com.easybank.account.entity.AccountEntity;
import com.easybank.account.exception.AccountAlreadyExistsException;
import com.easybank.account.exception.ResourceNotFoundException;
import com.easybank.account.mapper.AccountMapper;
import com.easybank.account.repository.AccountRepository;
import com.easybank.account.service.AccountService;
import com.easybank.common.event.AccountDataChangedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    private final EventGateway eventGateway;

    /**
     * @param accountDto - AccountDto Object to create
     */
    @Override
    public void create(AccountDto accountDto) {
        String mobileNumber = accountDto.getMobileNumber();

        Optional<AccountEntity> optionalAccounts =
                accountRepository.findByMobileNumberAndActiveSw(
                        mobileNumber, AccountsConstants.ACTIVE_SW);

        if(optionalAccounts.isPresent()){
            throw new AccountAlreadyExistsException("Account already registered with given mobileNumber " + mobileNumber);
        }

        AccountEntity newAccountEntity = new AccountEntity();
        BeanUtils.copyProperties(accountDto, newAccountEntity);

        accountRepository.save(newAccountEntity);
    }

    /**
     * @param mobileNumber - Input Mobile Number
     * @return Accounts Details based on a given mobileNumber
     */
    @Override
    public AccountDto fetch(String mobileNumber) {
        AccountEntity accountEntity = accountRepository.findByMobileNumberAndActiveSw(
                mobileNumber, AccountsConstants.ACTIVE_SW)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Account", "mobileNumber", mobileNumber)
        );
        AccountDto accountDto = AccountMapper.mapToDto(accountEntity, new AccountDto());

        log.info("Account details for mobileNumber [{}] is [{}].",
                mobileNumber, accountDto);

        return accountDto;
    }

    /**
     * @param accountDto - AccountsDto Object
     * @return boolean indicating if the update of Account details is successful or not
     */
    @Override
    public boolean update(AccountDto accountDto) {

        AccountEntity accountEntity = accountRepository.findByMobileNumberAndActiveSw(
                accountDto.getMobileNumber(), AccountsConstants.ACTIVE_SW)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Account", "mobileNumber",
                                accountDto.getMobileNumber()));

        AccountMapper.mapToEntity(accountDto, accountEntity);
        accountRepository.save(accountEntity);

        return true;
    }

    /**
     * @param accountNumber - Input Account Number
     * @return boolean indicating if the delete of Account details is successful or not
     */
    @Override
    public boolean delete(Long accountNumber) {
        AccountEntity accountEntity = accountRepository.findById(accountNumber)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Account", "accountNumber",
                                accountNumber.toString()));

        accountEntity.setActiveSw(AccountsConstants.IN_ACTIVE_SW);
        accountRepository.save(accountEntity);

        // To publish the data changed event when customer is deleted (logically)
        // and the changes can be shown their by profile microservice.
        AccountDataChangedEvent accountDataChangedEvent =
                AccountDataChangedEvent.builder()
                        .mobileNumber(accountEntity.getMobileNumber())
                        .accountNumber(0L)
                        .build();

        eventGateway.publish(accountDataChangedEvent);

        return true;
    }

}
