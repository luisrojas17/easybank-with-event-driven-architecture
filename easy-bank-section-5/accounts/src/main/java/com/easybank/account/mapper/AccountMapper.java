package com.easybank.account.mapper;

import com.easybank.account.dto.AccountDto;
import com.easybank.account.entity.AccountEntity;

public class AccountMapper {

    public static AccountDto mapToDto(AccountEntity accountEntity, AccountDto accountDto) {
        accountDto.setAccountNumber(accountEntity.getAccountNumber());
        accountDto.setMobileNumber(accountEntity.getMobileNumber());
        accountDto.setAccountType(accountEntity.getAccountType());
        accountDto.setBranchAddress(accountEntity.getBranchAddress());
        accountDto.setActiveSw(accountEntity.isActiveSw());
        return accountDto;
    }

    public static AccountEntity mapToEntity(AccountDto accountDto, AccountEntity accountEntity) {
        accountEntity.setAccountType(accountDto.getAccountType());
        accountEntity.setBranchAddress(accountDto.getBranchAddress());
        return accountEntity;
    }

}
