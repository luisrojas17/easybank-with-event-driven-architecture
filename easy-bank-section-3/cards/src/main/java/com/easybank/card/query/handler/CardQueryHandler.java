package com.easybank.card.query.handler;

import com.easybank.card.dto.CardDto;
import com.easybank.card.query.FindCardQuery;
import com.easybank.card.service.CardsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CardQueryHandler {

    private final CardsService cardsService;

    @QueryHandler
    public CardDto findCard(FindCardQuery query) {

        log.info("Processing FindCardQuery.\n[{}]", query);

        CardDto cardDto = cardsService.fetch(query.getMobileNumber());

        log.info("Card details were gotten for mobileNumber [{}] are [{}]",
                query.getMobileNumber(), cardDto);

        return cardDto;
    }
}
