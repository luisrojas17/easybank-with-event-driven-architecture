package com.easybank.loan.mapper;

import com.easybank.loan.dto.LoanDto;
import com.easybank.loan.entity.LoanEntity;

public class LoansMapper {

    public static LoanDto mapToDto(LoanEntity loans, LoanDto loanDto) {
        loanDto.setLoanNumber(loans.getLoanNumber());
        loanDto.setLoanType(loans.getLoanType());
        loanDto.setMobileNumber(loans.getMobileNumber());
        loanDto.setTotalLoan(loans.getTotalLoan());
        loanDto.setAmountPaid(loans.getAmountPaid());
        loanDto.setOutstandingAmount(loans.getOutstandingAmount());
        loanDto.setActiveSw(loans.isActiveSw());
        return loanDto;
    }

    public static LoanEntity mapToEntity(LoanDto loanDto, LoanEntity loans) {
        loans.setLoanType(loanDto.getLoanType());
        loans.setTotalLoan(loanDto.getTotalLoan());
        loans.setAmountPaid(loanDto.getAmountPaid());
        loans.setOutstandingAmount(loanDto.getOutstandingAmount());
        return loans;
    }

}
