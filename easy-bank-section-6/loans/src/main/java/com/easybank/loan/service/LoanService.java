package com.easybank.loan.service;

import com.easybank.loan.dto.LoanDto;

public interface LoanService {

    void create(LoanDto loanDto);

    LoanDto fetch(String mobileNumber);

    boolean update(LoanDto loanDto);

    boolean delete(Long loanNumber);

    boolean updateMobileNumber(String oldMobileNumber, String newMobileNumber);

}
