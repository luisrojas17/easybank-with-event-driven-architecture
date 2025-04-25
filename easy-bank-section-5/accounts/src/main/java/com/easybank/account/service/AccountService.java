package com.easybank.account.service;

import com.easybank.account.dto.AccountDto;
import com.easybank.common.dto.MobileNumberToUpdateDto;

public interface AccountService {

    void create(String mobileNumber);

    AccountDto fetch(String mobileNumber);

    boolean update(AccountDto accountDto);

    boolean delete(Long accountNumber);

    boolean updateMobileNumber(MobileNumberToUpdateDto mobileNumberToUpdateDto);
}
