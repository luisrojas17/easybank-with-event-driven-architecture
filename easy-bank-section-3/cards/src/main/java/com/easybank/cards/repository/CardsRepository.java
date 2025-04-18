package com.easybank.cards.repository;

import com.easybank.cards.entity.CardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardsRepository extends JpaRepository<CardEntity, Long> {

    Optional<CardEntity> findByMobileNumberAndActiveSw(String mobileNumber, boolean activeSw);

    Optional<CardEntity> findByCardNumberAndActiveSw(Long cardNumber, boolean activeSw);

}
