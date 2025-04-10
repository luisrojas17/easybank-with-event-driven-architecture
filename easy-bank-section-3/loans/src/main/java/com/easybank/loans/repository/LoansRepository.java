package com.easybank.loans.repository;

import com.easybank.loans.entity.Loans;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoansRepository extends JpaRepository<Loans, Long> {

    Optional<Loans> findByMobileNumberAndActiveSw(String mobileNumber, boolean activeSw);

}
