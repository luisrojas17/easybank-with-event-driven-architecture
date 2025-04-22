package com.easybank.loans.query.handler;

import com.easybank.loans.dto.LoanDto;
import com.easybank.loans.query.FindLoanQuery;
import com.easybank.loans.service.LoanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoanQueryHandler {

    private final LoanService loanService;

    @QueryHandler
    public LoanDto fetch(FindLoanQuery query) {

        log.info("Processing FindLoanQuery.\n[{}]", query);

        LoanDto loanDto = loanService.fetch(query.getMobileNumber());

        log.info("Loan details were gotten for mobileNumber [{}] are [{}]",
                query.getMobileNumber(), loanDto);

        return loanDto;
    }
}
