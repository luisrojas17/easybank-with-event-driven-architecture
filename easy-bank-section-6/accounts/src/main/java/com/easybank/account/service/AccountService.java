package com.easybank.account.service;

import com.easybank.account.dto.AccountDto;

public interface AccountService {

    void create(AccountDto accountDto);

    AccountDto fetch(String mobileNumber);

    boolean update(AccountDto accountDto);

    boolean delete(Long accountNumber);

    boolean updateMobileNumber(String oldMobileNumber, String newMobileNumber);

}
