package com.easybank.account.service;

import com.easybank.account.dto.AccountDto;

public interface AccountService {

    /**
     *
     * @param accountDto - AccountDto Object to create
     */
    void create(AccountDto accountDto);

    /**
     *
     * @param mobileNumber - Input Mobile Number
     * @return Accounts Details based on a given mobileNumber
     */
    AccountDto fetch(String mobileNumber);

    /**
     *
     * @param accountDto - AccountDto Object
     * @return boolean indicating if the update of Account details is successful or not
     */
    boolean update(AccountDto accountDto);

    /**
     *
     * @param accountNumber - Input Account Number
     * @return boolean indicating if the delete of Account details is successful or not
     */
    boolean delete(Long accountNumber);


}
