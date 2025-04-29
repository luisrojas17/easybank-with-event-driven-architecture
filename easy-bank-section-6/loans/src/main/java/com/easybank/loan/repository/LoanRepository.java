package com.easybank.loan.repository;

import com.easybank.loan.entity.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<LoanEntity, Long> {

    Optional<LoanEntity> findByMobileNumberAndActiveSw(String mobileNumber, boolean activeSw);

    Optional<LoanEntity> findByLoanNumberAndActiveSw(Long loanNumber, boolean activeSw);

}
