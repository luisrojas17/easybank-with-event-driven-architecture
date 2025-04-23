package com.easybank.loan.query.projection;

import com.easybank.loan.command.event.LoanCreatedEvent;
import com.easybank.loan.command.event.LoanDeletedEvent;
import com.easybank.loan.command.event.LoanUpdatedEvent;
import com.easybank.loan.dto.LoanDto;
import com.easybank.loan.service.LoanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@ProcessingGroup("loan-group")
public class LoanProjection {

    private final LoanService loanService;

    @EventHandler
    public void handler(LoanCreatedEvent loanCreatedEvent) {

        log.info("Processing LoanCreatedEvent.\n\t[{}]", loanCreatedEvent);

        LoanDto loanDto = new LoanDto();

        BeanUtils.copyProperties(loanCreatedEvent, loanDto);

        loanService.create(loanDto);

        log.info("LoanCreatedEvent processed successfully.");
    }

    @EventHandler
    public void handler(LoanUpdatedEvent loanUpdatedEvent) {
        log.info("Processing LoanUpdatedEvent.\n\t[{}]", loanUpdatedEvent.getLoanNumber());

        LoanDto loanDto = new LoanDto();

        BeanUtils.copyProperties(loanUpdatedEvent, loanDto);

        boolean result = loanService.update(loanDto);

        log.info("LoanUpdatedEvent processed successfully [{}].", result);
    }

    @EventHandler
    public void handler(LoanDeletedEvent loanDeletedEvent) {
        log.info("Processing LoanDeletedEvent.\n\t[{}]", loanDeletedEvent.getLoanNumber());

        boolean result = loanService.delete(loanDeletedEvent.getLoanNumber());

        log.info("LoanDeletedEvent processed successfully [{}].", result);
    }
}
