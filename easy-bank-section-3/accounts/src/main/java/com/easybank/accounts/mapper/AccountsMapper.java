package com.easybank.accounts.mapper;

import com.easybank.accounts.dto.AccountDto;
import com.easybank.accounts.entity.AccountEntity;

public class AccountsMapper {

    public static AccountDto mapToDto(AccountEntity accounts, AccountDto accountDto) {
        accountDto.setAccountNumber(accounts.getAccountNumber());
        accountDto.setMobileNumber(accounts.getMobileNumber());
        accountDto.setAccountType(accounts.getAccountType());
        accountDto.setBranchAddress(accounts.getBranchAddress());
        accountDto.setActiveSw(accounts.isActiveSw());
        return accountDto;
    }

    public static AccountEntity mapToEntity(AccountDto accountDto, AccountEntity accounts) {
        accounts.setAccountType(accountDto.getAccountType());
        accounts.setBranchAddress(accountDto.getBranchAddress());
        return accounts;
    }

}
