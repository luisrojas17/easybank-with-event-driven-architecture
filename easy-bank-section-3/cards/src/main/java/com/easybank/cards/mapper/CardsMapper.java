package com.easybank.cards.mapper;

import com.easybank.cards.dto.CardDto;
import com.easybank.cards.entity.CardEntity;

public class CardsMapper {

    public static CardDto mapToCardsDto(CardEntity cards, CardDto cardDto) {
        cardDto.setCardNumber(cards.getCardNumber());
        cardDto.setCardType(cards.getCardType());
        cardDto.setMobileNumber(cards.getMobileNumber());
        cardDto.setTotalLimit(cards.getTotalLimit());
        cardDto.setAvailableAmount(cards.getAvailableAmount());
        cardDto.setAmountUsed(cards.getAmountUsed());
        cardDto.setActiveSw(cards.isActiveSw());
        return cardDto;
    }

    public static CardEntity mapToCards(CardDto cardDto, CardEntity cards) {
        cards.setCardType(cardDto.getCardType());
        cards.setTotalLimit(cardDto.getTotalLimit());
        cards.setAvailableAmount(cardDto.getAvailableAmount());
        cards.setAmountUsed(cardDto.getAmountUsed());
        return cards;
    }

}
