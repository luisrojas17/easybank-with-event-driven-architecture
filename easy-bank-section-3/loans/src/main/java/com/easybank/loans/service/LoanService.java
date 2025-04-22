package com.easybank.loans.service;

import com.easybank.loans.dto.LoanDto;

public interface LoanService {

    void create(LoanDto loanDto);

    LoanDto fetch(String mobileNumber);

    boolean update(LoanDto loanDto);

    boolean delete(Long loanNumber);

}
