package com.easybank.cards.service.impl;

import com.easybank.cards.constants.CardsConstants;
import com.easybank.cards.dto.CardDto;
import com.easybank.cards.entity.CardEntity;
import com.easybank.cards.exception.CardAlreadyExistsException;
import com.easybank.cards.exception.ResourceNotFoundException;
import com.easybank.cards.mapper.CardsMapper;
import com.easybank.cards.repository.CardsRepository;
import com.easybank.cards.service.CardsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class CardsServiceImpl implements CardsService {

    private CardsRepository cardsRepository;

    /**
     * @param cardDto - CardsDto Object to create
     */
    @Override
    public void create(CardDto cardDto) {
        String mobileNumber = cardDto.getMobileNumber();

        Optional<CardEntity> optionalCard =
                cardsRepository.findByMobileNumberAndActiveSw(mobileNumber,
                    CardsConstants.ACTIVE_SW);

        if (optionalCard.isPresent()) {
            throw new CardAlreadyExistsException("Card already registered with given mobileNumber " + mobileNumber);
        }

        CardEntity entity = new CardEntity();

        BeanUtils.copyProperties(cardDto, entity);

        cardsRepository.save(entity);
    }

    /**
     * @param mobileNumber - Input mobile Number
     * @return Card Details based on a given mobileNumber
     */
    @Override
    public CardDto fetch(String mobileNumber) {
        CardEntity card = cardsRepository.findByMobileNumberAndActiveSw(
                mobileNumber, CardsConstants.ACTIVE_SW)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Card", "mobileNumber", mobileNumber));

        CardDto cardDto = CardsMapper.mapToCardsDto(card, new CardDto());

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
        CardEntity cardEntity = cardsRepository.findByMobileNumberAndActiveSw(
                cardDto.getMobileNumber(), CardsConstants.ACTIVE_SW)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Card", "CardNumber",
                                cardDto.getCardNumber().toString()));

        CardsMapper.mapToCards(cardDto, cardEntity);
        cardsRepository.save(cardEntity);

        return true;
    }

    /**
     * @param cardNumber - Input Card Number
     * @return boolean indicating if the delete of card details is successful or not
     */
    @Override
    public boolean delete(Long cardNumber) {
        CardEntity card = cardsRepository.findById(cardNumber)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Card", "cardNumber",
                                cardNumber.toString()));

        card.setActiveSw(CardsConstants.IN_ACTIVE_SW);
        cardsRepository.save(card);

        return true;
    }


}
