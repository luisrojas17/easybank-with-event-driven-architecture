package com.easybank.card.service;

import com.easybank.card.dto.CardDto;
import com.easybank.common.dto.MobileNumberToUpdateDto;

public interface CardService {

    void create(String mobileNumber);

    CardDto fetch(String mobileNumber);

    boolean update(CardDto cardDto);

    boolean delete(Long cardNumber);

    boolean updateMobileNumber(MobileNumberToUpdateDto mobileNumberToUpdateDto);

}
