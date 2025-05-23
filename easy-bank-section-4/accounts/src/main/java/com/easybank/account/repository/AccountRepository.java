package com.easybank.account.repository;

import com.easybank.account.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

    Optional<AccountEntity> findByMobileNumberAndActiveSw(String mobileNumber, boolean active);

    Optional<AccountEntity> findByAccountNumberAndActiveSw(long accountNumber, boolean active);

}
