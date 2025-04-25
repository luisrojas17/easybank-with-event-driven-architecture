package com.easybank.card.repository;

import com.easybank.card.entity.CardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<CardEntity, Long> {

    Optional<CardEntity> findByMobileNumberAndActiveSw(String mobileNumber, boolean activeSw);

}
