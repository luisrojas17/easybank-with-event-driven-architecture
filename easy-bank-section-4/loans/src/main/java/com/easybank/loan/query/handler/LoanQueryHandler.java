package com.easybank.loan.query.handler;

import com.easybank.loan.dto.LoanDto;
import com.easybank.loan.query.FindLoanQuery;
import com.easybank.loan.service.LoanService;
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
