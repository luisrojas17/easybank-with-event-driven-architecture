package com.easybank.loans.service.impl;

import com.easybank.common.dto.MobileNumberToUpdateDto;
import com.easybank.loans.constants.LoansConstants;
import com.easybank.loans.dto.LoanDto;
import com.easybank.loans.entity.LoanEntity;
import com.easybank.loans.exception.LoanAlreadyExistsException;
import com.easybank.loans.exception.ResourceNotFoundException;
import com.easybank.loans.mapper.LoanMapper;
import com.easybank.loans.repository.LoanRepository;
import com.easybank.loans.service.LoanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;

    private final StreamBridge streamBridge;

    @Override
    public void create(String mobileNumber) {
        Optional<LoanEntity> optionalLoan = loanRepository.findByMobileNumberAndActiveSw(mobileNumber,
                LoansConstants.ACTIVE_SW);
        if (optionalLoan.isPresent()) {
            throw new LoanAlreadyExistsException("Loan already registered with given mobileNumber " + mobileNumber);
        }
        loanRepository.save(createNewLoan(mobileNumber));
    }

    private LoanEntity createNewLoan(String mobileNumber) {
        LoanEntity newLoan = new LoanEntity();
        long randomLoanNumber = 1000000000L + new Random().nextInt(900000000);
        newLoan.setLoanNumber(randomLoanNumber);
        newLoan.setMobileNumber(mobileNumber);
        newLoan.setLoanType(LoansConstants.HOME_LOAN);
        newLoan.setTotalLoan(LoansConstants.NEW_LOAN_LIMIT);
        newLoan.setAmountPaid(0);
        newLoan.setOutstandingAmount(LoansConstants.NEW_LOAN_LIMIT);
        newLoan.setActiveSw(LoansConstants.ACTIVE_SW);
        return newLoan;
    }

    @Override
    public LoanDto fetch(String mobileNumber) {
        LoanEntity loan =
                loanRepository.findByMobileNumberAndActiveSw(mobileNumber, LoansConstants.ACTIVE_SW)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Loan", "mobileNumber", mobileNumber));

        return LoanMapper.mapToDto(loan, new LoanDto());
    }

    @Override
    public boolean update(LoanDto loanDto) {
        LoanEntity loan = loanRepository.findByMobileNumberAndActiveSw(loanDto.getMobileNumber(),
                LoansConstants.ACTIVE_SW).orElseThrow(
                () -> new ResourceNotFoundException("Loan", "LoanNumber", loanDto.getLoanNumber().toString()));
        LoanMapper.mapToEntity(loanDto, loan);
        loanRepository.save(loan);

        return true;
    }

    @Override
    public boolean delete(Long loanNumber) {
        LoanEntity loan = loanRepository.findById(loanNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Loan", "loanNumber", loanNumber.toString())
                );
        loan.setActiveSw(LoansConstants.IN_ACTIVE_SW);
        loanRepository.save(loan);
        return true;
    }

    @Override
    public boolean updateMobileNumber(MobileNumberToUpdateDto mobileNumberToUpdateDto) {

        log.info("Updating mobile number [{}].",
                mobileNumberToUpdateDto.getCurrentMobileNumber());

        LoanEntity loanEntity =
                loanRepository.findByMobileNumberAndActiveSw(
                        mobileNumberToUpdateDto.getCurrentMobileNumber(), LoansConstants.ACTIVE_SW)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Loan", "mobileNumber",
                                        mobileNumberToUpdateDto.getCurrentMobileNumber()));

        loanEntity.setMobileNumber(mobileNumberToUpdateDto.getNewMobileNumber());
        loanRepository.save(loanEntity);

        log.info("Mobile number [{}] was updated to [{}] successfully.",
                mobileNumberToUpdateDto.getCurrentMobileNumber(), mobileNumberToUpdateDto.getNewMobileNumber());

        publishEvent(mobileNumberToUpdateDto);

        return true;
    }

    /**
     * This method publish status event into a Queue in order to update the mobile number status
     * for all Microservice and keeping the data consistency through all Microservices' Databases.
     *
     * @param mobileNumberToUpdateDto an instance of MobileNumberToUpdateDto which contains current and new
     *                                mobile number to be updated.
     */
    private void publishEvent(MobileNumberToUpdateDto mobileNumberToUpdateDto) {

        log.info("Publishing event to update mobile number [{}].",
                mobileNumberToUpdateDto);

        var result =
                streamBridge.send("updateMobileNumberStatus-out-0", mobileNumberToUpdateDto);

        log.info("Event published successfully [{}].", result);
    }

}
