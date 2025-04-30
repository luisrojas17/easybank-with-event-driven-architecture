package com.easybank.card.command.aggregate;

import com.easybank.card.command.CreateCardCommand;
import com.easybank.card.command.DeleteCardCommand;
import com.easybank.card.command.UpdateCardCommand;
import com.easybank.card.command.event.CardCreatedEvent;
import com.easybank.card.command.event.CardDeletedEvent;
import com.easybank.card.command.event.CardUpdatedEvent;
import com.easybank.common.command.RollbackCardMobileNumberCommand;
import com.easybank.common.command.UpdateCardMobileNumberCommand;
import com.easybank.common.event.CardMobileNumberRollbackedEvent;
import com.easybank.common.event.CardMobileNumberUpdatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

@Slf4j
@Aggregate
public class CardAggregate {

    @AggregateIdentifier
    private Long cardNumber;
    private String mobileNumber;
    private String cardType;
    private int totalLimit;
    private int amountUsed;
    private int availableAmount;
    private boolean activeSw;
    private String errorMessage;

    public CardAggregate() {}

    @CommandHandler
    public CardAggregate(CreateCardCommand createCommand) {
        CardCreatedEvent cardCreatedEvent = new CardCreatedEvent();
        BeanUtils.copyProperties(createCommand, cardCreatedEvent);
        AggregateLifecycle.apply(cardCreatedEvent);
    }

    @EventSourcingHandler
    public void on(CardCreatedEvent cardCreatedEvent) {
        this.cardNumber = cardCreatedEvent.getCardNumber();
        this.mobileNumber = cardCreatedEvent.getMobileNumber();
        this.cardType = cardCreatedEvent.getCardType();
        this.totalLimit = cardCreatedEvent.getTotalLimit();
        this.amountUsed = cardCreatedEvent.getAmountUsed();
        this.availableAmount = cardCreatedEvent.getAvailableAmount();
        this.activeSw = cardCreatedEvent.isActiveSw();
    }

    @CommandHandler
    public void handler(UpdateCardCommand updateCommand) {
        CardUpdatedEvent cardUpdatedEvent = new CardUpdatedEvent();
        BeanUtils.copyProperties(updateCommand, cardUpdatedEvent);
        AggregateLifecycle.apply(cardUpdatedEvent);
    }

    @EventSourcingHandler
    public void on(CardUpdatedEvent cardUpdatedEvent) {
        this.cardType = cardUpdatedEvent.getCardType();
        this.totalLimit = cardUpdatedEvent.getTotalLimit();
        this.amountUsed = cardUpdatedEvent.getAmountUsed();
        this.availableAmount = cardUpdatedEvent.getAvailableAmount();
    }

    @CommandHandler
    public void handler(DeleteCardCommand deleteCommand) {
        CardDeletedEvent cardDeletedEvent = new CardDeletedEvent();
        BeanUtils.copyProperties(deleteCommand, cardDeletedEvent);
        AggregateLifecycle.apply(cardDeletedEvent);
    }

    @EventSourcingHandler
    public void on(CardDeletedEvent cardDeletedEvent) {
        this.activeSw = cardDeletedEvent.isActiveSw();
    }

    @CommandHandler
    public void handler(UpdateCardMobileNumberCommand updateCardMobileNumberCommand) {

        log.info("Processing UpdateCardMobileNumberCommand.\n\t[{}]",
                updateCardMobileNumberCommand);

        // To create an event
        CardMobileNumberUpdatedEvent cardMobileNumberUpdatedEvent =
                new CardMobileNumberUpdatedEvent();

        // To copy all data properties
        BeanUtils.copyProperties(updateCardMobileNumberCommand, cardMobileNumberUpdatedEvent);

        // To publish the event
        AggregateLifecycle.apply(cardMobileNumberUpdatedEvent);

        log.info("UpdateCardMobileNumberCommand processed successfully.");
    }

    @EventSourcingHandler
    public void on(CardMobileNumberUpdatedEvent cardMobileNumberUpdatedEvent) {

        this.mobileNumber = cardMobileNumberUpdatedEvent.getNewMobileNumber();
    }

    @CommandHandler
    public void handler(RollbackCardMobileNumberCommand rollbackCardMobileNumberCommand) {

        log.info("Processing RollbackCardMobileNumberCommand.\n\t[{}]",
                rollbackCardMobileNumberCommand);

        // To create an event
        CardMobileNumberRollbackedEvent cardMobileNumberRollbackedEvent =
                new CardMobileNumberRollbackedEvent();

        // To copy all data properties
        BeanUtils.copyProperties(rollbackCardMobileNumberCommand, cardMobileNumberRollbackedEvent);

        // To publish new event which is handled by CardProjection.
        // However, SagaManager orchestrates this event in order to continue with the flow in reverse order to complete
        // the compensation transaction.
        AggregateLifecycle.apply(cardMobileNumberRollbackedEvent);

        log.info("RollbackCardMobileNumberCommand processed successfully.");
    }

    @EventSourcingHandler
    public void on(CardMobileNumberRollbackedEvent cardMobileNumberRollbackedEvent) {

        this.mobileNumber = cardMobileNumberRollbackedEvent.getCurrentMobileNumber();
        this.errorMessage = cardMobileNumberRollbackedEvent.getErrorMessage();
    }

}
