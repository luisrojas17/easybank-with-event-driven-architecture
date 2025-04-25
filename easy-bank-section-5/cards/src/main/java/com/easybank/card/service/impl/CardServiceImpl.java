package com.easybank.card.service.impl;

import com.easybank.card.constants.CardsConstants;
import com.easybank.card.dto.CardDto;
import com.easybank.card.entity.CardEntity;
import com.easybank.card.exception.CardAlreadyExistsException;
import com.easybank.card.exception.ResourceNotFoundException;
import com.easybank.card.mapper.CardMapper;
import com.easybank.card.repository.CardRepository;
import com.easybank.card.service.CardService;
import com.easybank.common.dto.MobileNumberToUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;

    private final StreamBridge streamBridge;

    /**
     * @param mobileNumber - Mobile Number of the Customer
     */
    @Override
    public void create(String mobileNumber) {
        Optional<CardEntity> optionalCard = cardRepository.findByMobileNumberAndActiveSw(mobileNumber,
                CardsConstants.ACTIVE_SW);
        if (optionalCard.isPresent()) {
            throw new CardAlreadyExistsException("Card already registered with given mobileNumber " + mobileNumber);
        }
        cardRepository.save(createNewCard(mobileNumber));
    }

    /**
     * @param mobileNumber - Mobile Number of the Customer
     * @return the new card details
     */
    private CardEntity createNewCard(String mobileNumber) {
        CardEntity newCard = new CardEntity();
        long randomCardNumber = 100000000000L + new Random().nextInt(900000000);
        newCard.setCardNumber(randomCardNumber);
        newCard.setMobileNumber(mobileNumber);
        newCard.setCardType(CardsConstants.CREDIT_CARD);
        newCard.setTotalLimit(CardsConstants.NEW_CARD_LIMIT);
        newCard.setAmountUsed(0);
        newCard.setAvailableAmount(CardsConstants.NEW_CARD_LIMIT);
        newCard.setActiveSw(CardsConstants.ACTIVE_SW);
        return newCard;
    }

    /**
     * @param mobileNumber - Input mobile Number
     * @return Card Details based on a given mobileNumber
     */
    @Override
    public CardDto fetch(String mobileNumber) {
        CardEntity card =
                cardRepository.findByMobileNumberAndActiveSw(mobileNumber, CardsConstants.ACTIVE_SW)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Card", "mobileNumber", mobileNumber));

        return CardMapper.mapToDto(card, new CardDto());
    }

    /**
     * @param cardDto - CardsDto Object
     * @return boolean indicating if the update of card details is successful or not
     */
    @Override
    public boolean update(CardDto cardDto) {
        CardEntity card =
                cardRepository.findByMobileNumberAndActiveSw(
                        cardDto.getMobileNumber(), CardsConstants.ACTIVE_SW)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Card", "CardNumber", cardDto.getCardNumber().toString()));

        CardMapper.mapToEntity(cardDto, card);
        cardRepository.save(card);

        return true;
    }

    @Override
    public boolean delete(Long cardNumber) {
        CardEntity card = cardRepository.findById(cardNumber)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Card", "cardNumber", cardNumber.toString()));
        card.setActiveSw(CardsConstants.IN_ACTIVE_SW);
        cardRepository.save(card);

        return true;
    }

    @Override
    public boolean updateMobileNumber(MobileNumberToUpdateDto mobileNumberToUpdateDto) {

        log.info("Updating mobile number [{}].",
                mobileNumberToUpdateDto.getCurrentMobileNumber());

        CardEntity cardEntity =
                cardRepository.findByMobileNumberAndActiveSw(
                        mobileNumberToUpdateDto.getCurrentMobileNumber(), CardsConstants.ACTIVE_SW)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Card", "mobileNumber",
                                        mobileNumberToUpdateDto.getCurrentMobileNumber()));

        cardEntity.setMobileNumber(mobileNumberToUpdateDto.getNewMobileNumber());
        cardRepository.save(cardEntity);

        log.info("Mobile number [{}] was updated to [{}] successfully.",
                mobileNumberToUpdateDto.getCurrentMobileNumber(), mobileNumberToUpdateDto.getNewMobileNumber());

        publishEvent(mobileNumberToUpdateDto);

        return true;
    }

    /**
     * This method publish an event in to Loan Queue in order to update the mobile number
     * for Loan Microservice and keeping the data consistency through all Microservices' Databases.
     *
     * @param mobileNumberToUpdateDto an instance of MobileNumberToUpdateDto which contains current and new
     *                                mobile number to be updated.
     */
    private void publishEvent(MobileNumberToUpdateDto mobileNumberToUpdateDto) {

        log.info("Publishing event to update mobile number [{}].",
                mobileNumberToUpdateDto);

        var result =
                streamBridge.send("updateLoanMobileNumber-out-0", mobileNumberToUpdateDto);

        log.info("Event published successfully [{}].", result);
    }

}
