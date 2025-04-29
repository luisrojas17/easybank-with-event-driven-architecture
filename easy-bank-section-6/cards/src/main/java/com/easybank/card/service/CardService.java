package com.easybank.card.service;

import com.easybank.card.dto.CardDto;

public interface CardService {

    void create(CardDto cardDto);

    CardDto fetch(String mobileNumber);

    boolean update(CardDto cardDto);

    boolean delete(Long cardNumber);

    boolean updateMobileNumber(String oldMobileNumber, String newMobileNumber);

}
