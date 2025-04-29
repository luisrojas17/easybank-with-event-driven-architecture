package com.easybank.account.service.impl;

import com.easybank.account.constants.AccountsConstants;
import com.easybank.account.dto.AccountDto;
import com.easybank.account.entity.AccountEntity;
import com.easybank.account.exception.AccountAlreadyExistsException;
import com.easybank.account.exception.ResourceNotFoundException;
import com.easybank.account.mapper.AccountMapper;
import com.easybank.account.repository.AccountRepository;
import com.easybank.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

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

    @Override
    public boolean delete(Long accountNumber) {
        AccountEntity accountEntity = accountRepository.findById(accountNumber)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Account", "accountNumber",
                                accountNumber.toString()));

        accountEntity.setActiveSw(AccountsConstants.IN_ACTIVE_SW);
        accountRepository.save(accountEntity);

        return true;
    }

    @Override
    public boolean updateMobileNumber(String oldMobileNumber, String newMobileNumber) {
        AccountEntity accountEntity =
                accountRepository.findByMobileNumberAndActiveSw(oldMobileNumber, AccountsConstants.ACTIVE_SW)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Account", "mobileNumber", oldMobileNumber));

        accountEntity.setMobileNumber(newMobileNumber);
        accountRepository.save(accountEntity);

        return true;
    }
}
