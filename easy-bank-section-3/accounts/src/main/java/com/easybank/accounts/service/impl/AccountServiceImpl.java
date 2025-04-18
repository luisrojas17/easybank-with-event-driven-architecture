package com.easybank.accounts.service.impl;

import com.easybank.accounts.constants.AccountsConstants;
import com.easybank.accounts.dto.AccountDto;
import com.easybank.accounts.entity.AccountEntity;
import com.easybank.accounts.exception.AccountAlreadyExistsException;
import com.easybank.accounts.exception.ResourceNotFoundException;
import com.easybank.accounts.mapper.AccountsMapper;
import com.easybank.accounts.repository.AccountRepository;
import com.easybank.accounts.service.AccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;

    /**
     * @param accountDto - AccountDto Object to create
     */
    @Override
    public void create(AccountDto accountDto) {
        String mobileNumber = accountDto.getMobileNumber();

        Optional<AccountEntity> optionalAccounts =
                accountRepository.findByMobileNumberAndActiveSw(mobileNumber,
                    AccountsConstants.ACTIVE_SW);

        if(optionalAccounts.isPresent()){
            throw new AccountAlreadyExistsException("Account already registered with given mobileNumber "+mobileNumber);
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
        AccountDto accountDto = AccountsMapper.mapToAccountsDto(accountEntity, new AccountDto());

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

        AccountsMapper.mapToAccounts(accountDto, accountEntity);
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

        return true;
    }

}
