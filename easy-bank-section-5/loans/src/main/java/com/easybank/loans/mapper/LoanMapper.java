package com.easybank.loans.mapper;

import com.easybank.loans.dto.LoanDto;
import com.easybank.loans.entity.LoanEntity;

public class LoanMapper {

    public static LoanDto mapToDto(LoanEntity loanEntity, LoanDto loanDto) {
        loanDto.setLoanNumber(loanEntity.getLoanNumber());
        loanDto.setLoanType(loanEntity.getLoanType());
        loanDto.setMobileNumber(loanEntity.getMobileNumber());
        loanDto.setTotalLoan(loanEntity.getTotalLoan());
        loanDto.setAmountPaid(loanEntity.getAmountPaid());
        loanDto.setOutstandingAmount(loanEntity.getOutstandingAmount());
        loanDto.setActiveSw(loanEntity.isActiveSw());
        return loanDto;
    }

    public static LoanEntity mapToEntity(LoanDto loanDto, LoanEntity loanEntity) {
        loanEntity.setLoanType(loanDto.getLoanType());
        loanEntity.setTotalLoan(loanDto.getTotalLoan());
        loanEntity.setAmountPaid(loanDto.getAmountPaid());
        loanEntity.setOutstandingAmount(loanDto.getOutstandingAmount());
        return loanEntity;
    }

}
