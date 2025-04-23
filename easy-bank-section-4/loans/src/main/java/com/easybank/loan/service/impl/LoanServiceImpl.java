package com.easybank.loan.service.impl;

import com.easybank.common.event.LoanDataChangedEvent;
import com.easybank.loan.constants.LoanConstants;
import com.easybank.loan.dto.LoanDto;
import com.easybank.loan.entity.LoanEntity;
import com.easybank.loan.exception.LoanAlreadyExistsException;
import com.easybank.loan.exception.ResourceNotFoundException;
import com.easybank.loan.mapper.LoansMapper;
import com.easybank.loan.repository.LoanRepository;
import com.easybank.loan.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;

    private final EventGateway eventGateway;

    @Override
    public void create(LoanDto loanDto) {
        String mobileNumber = loanDto.getMobileNumber();

        Optional<LoanEntity> optionalLoan =
                loanRepository.findByMobileNumberAndActiveSw(
                        mobileNumber, LoanConstants.ACTIVE_SW);

        if (optionalLoan.isPresent()) {
            throw new LoanAlreadyExistsException("Loan already registered with given mobileNumber " + mobileNumber);
        }

        LoanEntity loanEntity = new LoanEntity();

        BeanUtils.copyProperties(loanDto, loanEntity);

        loanRepository.save(loanEntity);
    }

    @Override
    public LoanDto fetch(String mobileNumber) {
        LoanEntity loanEntity = loanRepository.findByMobileNumberAndActiveSw(
                mobileNumber, LoanConstants.ACTIVE_SW)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Loan", "mobileNumber", mobileNumber));

        return LoansMapper.mapToDto(loanEntity, new LoanDto());
    }

    @Override
    public boolean update(LoanDto loanDto) {
        LoanEntity loanEntity = loanRepository.findByMobileNumberAndActiveSw(
                loanDto.getMobileNumber(), LoanConstants.ACTIVE_SW)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Loan", "LoanNumber",
                                loanDto.getLoanNumber().toString()));

        LoansMapper.mapToEntity(loanDto, loanEntity);
        loanRepository.save(loanEntity);

        return true;
    }

    @Override
    public boolean delete(Long loanNumber) {
        LoanEntity loanEntity = loanRepository.findById(loanNumber)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Loan", "loanNumber",
                                loanNumber.toString()));

        loanEntity.setActiveSw(LoanConstants.IN_ACTIVE_SW);
        loanRepository.save(loanEntity);

        // To publish the data changed event when customer is deleted (logically)
        // and the changes can be shown their by profile microservice.
        LoanDataChangedEvent loanDataChangedEvent =
                LoanDataChangedEvent.builder()
                        .mobileNumber(loanEntity.getMobileNumber())
                        .loanNumber(loanEntity.getLoanNumber())
                        .build();

        eventGateway.publish(loanDataChangedEvent);

        return true;
    }


}
