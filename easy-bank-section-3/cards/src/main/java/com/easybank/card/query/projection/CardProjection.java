package com.easybank.card.query.projection;

import com.easybank.card.command.event.CardCreatedEvent;
import com.easybank.card.command.event.CardDeletedEvent;
import com.easybank.card.command.event.CardUpdatedEvent;
import com.easybank.card.dto.CardDto;
import com.easybank.card.service.CardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@ProcessingGroup("card-group")
public class CardProjection {

    private final CardService cardService;

    @EventHandler
    public void handler(CardCreatedEvent event) {

        log.info("Processing CardCreatedEvent.\n\t[{}]", event);

        CardDto dto = new CardDto();

        BeanUtils.copyProperties(event, dto);

        cardService.create(dto);

        log.info("CardCreatedEvent processed successfully.");
    }

    @EventHandler
    public void handler(CardUpdatedEvent event) {

        log.info("Processing CardUpdatedEvent.\n\t[{}]", event);

        CardDto dto = new CardDto();

        BeanUtils.copyProperties(event, dto);

        boolean result = cardService.update(dto);

        log.info("CardUpdatedEvent processed successfully [{}].", result);
    }

    @EventHandler
    public void handler(CardDeletedEvent event) {

        log.info("Processing CardDeletedEvent.\n\t[{}]",
                event.getCardNumber());

        boolean result = cardService.delete(event.getCardNumber());

        log.info("CardDeletedEvent processed successfully [{}].", result);
    }

}
