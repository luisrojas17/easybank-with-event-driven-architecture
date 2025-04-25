package com.easybank.account.service.impl;

import com.easybank.account.constants.AccountsConstants;
import com.easybank.account.dto.AccountDto;
import com.easybank.account.entity.AccountEntity;
import com.easybank.account.exception.AccountAlreadyExistsException;
import com.easybank.account.exception.ResourceNotFoundException;
import com.easybank.account.mapper.AccountMapper;
import com.easybank.account.repository.AccountRepository;
import com.easybank.account.service.AccountService;
import com.easybank.common.dto.MobileNumberToUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    private StreamBridge streamBridge;

    @Override
    public void create(String mobileNumber) {
        Optional<AccountEntity> optionalAccounts=
                accountRepository.findByMobileNumberAndActiveSw(mobileNumber, AccountsConstants.ACTIVE_SW);

        if(optionalAccounts.isPresent()){
            throw new AccountAlreadyExistsException("Account already registered with given mobileNumber "
                    + mobileNumber);
        }

        accountRepository.save(createNewAccount(mobileNumber));
    }

    private AccountEntity createNewAccount(String mobileNumber) {
        AccountEntity newAccount = new AccountEntity();
        newAccount.setMobileNumber(mobileNumber);
        long randomAccNumber = 1000000000L + new Random().nextInt(900000000);
        newAccount.setAccountNumber(randomAccNumber);
        newAccount.setAccountType(AccountsConstants.SAVINGS);
        newAccount.setBranchAddress(AccountsConstants.ADDRESS);
        newAccount.setActiveSw(AccountsConstants.ACTIVE_SW);
        return newAccount;
    }

    @Override
    public AccountDto fetch(String mobileNumber) {
        AccountEntity account =
                accountRepository.findByMobileNumberAndActiveSw(
                        mobileNumber, AccountsConstants.ACTIVE_SW)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Account", "mobileNumber", mobileNumber));

        AccountDto accountDto = AccountMapper.mapToDto(account, new AccountDto());

        log.info("Accounts details for mobileNumber [{}], [{}]",
                mobileNumber, accountDto);

        return accountDto;
    }

    @Override
    public boolean update(AccountDto accountDto) {
        AccountEntity account =
                accountRepository.findByMobileNumberAndActiveSw(
                        accountDto.getMobileNumber(), AccountsConstants.ACTIVE_SW)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Account", "mobileNumber",
                accountDto.getMobileNumber()));

        AccountMapper.mapToEntity(accountDto, account);
        accountRepository.save(account);

        return true;
    }

    @Override
    public boolean delete(Long accountNumber) {
        AccountEntity account = accountRepository.findById(accountNumber).orElseThrow(
                () -> new ResourceNotFoundException("Account", "accountNumber", accountNumber.toString())
        );

        account.setActiveSw(AccountsConstants.IN_ACTIVE_SW);
        accountRepository.save(account);

        return true;
    }

    @Override
    public boolean updateMobileNumber(MobileNumberToUpdateDto mobileNumberToUpdateDto) {

        log.info("Updating mobile number [{}].",
                mobileNumberToUpdateDto.getCurrentMobileNumber());

        AccountEntity accountEntity =
                accountRepository.findByMobileNumberAndActiveSw(
                        mobileNumberToUpdateDto.getCurrentMobileNumber(), AccountsConstants.ACTIVE_SW)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Account", "mobileNumber",
                                        mobileNumberToUpdateDto.getCurrentMobileNumber()));

        accountEntity.setMobileNumber(mobileNumberToUpdateDto.getNewMobileNumber());

        accountRepository.save(accountEntity);

        log.info("Mobile number [{}] was updated to [{}] successfully.",
                mobileNumberToUpdateDto.getCurrentMobileNumber(), mobileNumberToUpdateDto.getNewMobileNumber());

        publishEvent(mobileNumberToUpdateDto);

        return true;
    }

    /**
     * This method publish an event in to Card Queue in order to update the mobile number
     * for Card Microservice and keeping the data consistency through all Microservices' Databases.
     *
     * @param mobileNumberToUpdateDto an instance of MobileNumberToUpdateDto which contains current and new
     *                                mobile number to be updated.
     */
    private void publishEvent(MobileNumberToUpdateDto mobileNumberToUpdateDto) {

        log.info("Publishing event to update mobile number [{}].",
                mobileNumberToUpdateDto);

        var result =
                streamBridge.send("updateCardMobileNumber-out-0", mobileNumberToUpdateDto);

        log.info("Event published successfully [{}].", result);
    }

}
