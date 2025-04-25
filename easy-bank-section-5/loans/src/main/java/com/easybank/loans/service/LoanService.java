package com.easybank.loans.service;

import com.easybank.common.dto.MobileNumberToUpdateDto;
import com.easybank.loans.dto.LoanDto;

public interface LoanService {

    void create(String mobileNumber);

    LoanDto fetch(String mobileNumber);

    boolean update(LoanDto loanDto);

    boolean delete(Long loanNumber);

    boolean updateMobileNumber(MobileNumberToUpdateDto mobileNumberToUpdateDto);

}
