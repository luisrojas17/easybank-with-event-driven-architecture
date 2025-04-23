package com.easybank.card.service;

import com.easybank.card.dto.CardDto;

public interface CardsService {

    /**
     *
     * @param cardDto - CardsDto Object
     */
    void create(CardDto cardDto);

    /**
     *
     * @param mobileNumber - Input mobile Number
     *  @return Card Details based on a given mobileNumber
     */
    CardDto fetch(String mobileNumber);

    /**
     *
     * @param cardDto - CardsDto Object
     * @return boolean indicating if the update of card details is successful or not
     */
    boolean update(CardDto cardDto);

    /**
     *
     * @param cardNumber - Input Card Number
     * @return boolean indicating if the delete of card details is successful or not
     */
    boolean delete(Long cardNumber);

}
