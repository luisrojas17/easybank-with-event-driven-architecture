package com.easybank.card.service.impl;

import com.easybank.card.constants.CardsConstants;
import com.easybank.card.dto.CardDto;
import com.easybank.card.entity.CardEntity;
import com.easybank.card.exception.CardAlreadyExistsException;
import com.easybank.card.exception.ResourceNotFoundException;
import com.easybank.card.mapper.CardMapper;
import com.easybank.card.repository.CardRepository;
import com.easybank.card.service.CardService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class CardServiceImpl implements CardService {

    private CardRepository cardRepository;

    /**
     * @param cardDto - CardsDto Object to create
     */
    @Override
    public void create(CardDto cardDto) {
        String mobileNumber = cardDto.getMobileNumber();

        Optional<CardEntity> optionalCard =
                cardRepository.findByMobileNumberAndActiveSw(mobileNumber,
                    CardsConstants.ACTIVE_SW);

        if (optionalCard.isPresent()) {
            throw new CardAlreadyExistsException("Card already registered with given mobileNumber " + mobileNumber);
        }

        CardEntity entity = new CardEntity();

        BeanUtils.copyProperties(cardDto, entity);

        cardRepository.save(entity);
    }

    /**
     * @param mobileNumber - Input mobile Number
     * @return Card Details based on a given mobileNumber
     */
    @Override
    public CardDto fetch(String mobileNumber) {
        CardEntity card = cardRepository.findByMobileNumberAndActiveSw(
                mobileNumber, CardsConstants.ACTIVE_SW)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Card", "mobileNumber", mobileNumber));

        CardDto cardDto = CardMapper.mapToDto(card, new CardDto());

        log.info("Card details for mobileNumber [{}] is [{}].",
                mobileNumber, cardDto);

        return cardDto;
    }

    /**
     * @param cardDto - CardsDto Object
     * @return boolean indicating if the update of card details is successful or not
     */
    @Override
    public boolean update(CardDto cardDto) {
        CardEntity cardEntity = cardRepository.findByMobileNumberAndActiveSw(
                cardDto.getMobileNumber(), CardsConstants.ACTIVE_SW)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Card", "CardNumber",
                                cardDto.getCardNumber().toString()));

        CardMapper.mapToEntity(cardDto, cardEntity);
        cardRepository.save(cardEntity);

        return true;
    }

    /**
     * @param cardNumber - Input Card Number
     * @return boolean indicating if the delete of card details is successful or not
     */
    @Override
    public boolean delete(Long cardNumber) {
        CardEntity card = cardRepository.findById(cardNumber)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Card", "cardNumber",
                                cardNumber.toString()));

        card.setActiveSw(CardsConstants.IN_ACTIVE_SW);
        cardRepository.save(card);

        return true;
    }


}
