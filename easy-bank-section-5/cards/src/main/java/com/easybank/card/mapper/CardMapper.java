package com.easybank.card.mapper;

import com.easybank.card.dto.CardDto;
import com.easybank.card.entity.CardEntity;

public class CardMapper {

    public static CardDto mapToDto(CardEntity cardEntity, CardDto cardDto) {
        cardDto.setCardNumber(cardEntity.getCardNumber());
        cardDto.setCardType(cardEntity.getCardType());
        cardDto.setMobileNumber(cardEntity.getMobileNumber());
        cardDto.setTotalLimit(cardEntity.getTotalLimit());
        cardDto.setAvailableAmount(cardEntity.getAvailableAmount());
        cardDto.setAmountUsed(cardEntity.getAmountUsed());
        cardDto.setActiveSw(cardEntity.isActiveSw());
        return cardDto;
    }

    public static CardEntity mapToEntity(CardDto cardDto, CardEntity cardEntity) {
        cardEntity.setCardType(cardDto.getCardType());
        cardEntity.setTotalLimit(cardDto.getTotalLimit());
        cardEntity.setAvailableAmount(cardDto.getAvailableAmount());
        cardEntity.setAmountUsed(cardDto.getAmountUsed());
        return cardEntity;
    }

}
