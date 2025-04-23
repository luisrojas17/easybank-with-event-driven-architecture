package com.easybank.card.command.aggregate;

import com.easybank.card.command.CreateCardCommand;
import com.easybank.card.command.DeleteCardCommand;
import com.easybank.card.command.UpdateCardCommand;
import com.easybank.card.command.event.CardCreatedEvent;
import com.easybank.card.command.event.CardDeletedEvent;
import com.easybank.card.command.event.CardUpdatedEvent;
import com.easybank.common.event.CardDataChangedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

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

    public CardAggregate() {}

    @CommandHandler
    public CardAggregate(CreateCardCommand createCommand) {
        CardCreatedEvent cardCreatedEvent = new CardCreatedEvent();
        BeanUtils.copyProperties(createCommand, cardCreatedEvent);

        // Materialized View Pattern
        CardDataChangedEvent cardDataChangedEvent =
                CardDataChangedEvent.builder().build();
        BeanUtils.copyProperties(createCommand, cardDataChangedEvent);

        // To publish multiple events
        AggregateLifecycle.apply(cardCreatedEvent).andThen(
                () -> AggregateLifecycle.apply(cardDataChangedEvent)
        );
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

        // Materialized View Pattern
        CardDataChangedEvent cardDataChangedEvent =
                CardDataChangedEvent.builder().build();
        BeanUtils.copyProperties(updateCommand, cardDataChangedEvent);
        AggregateLifecycle.apply(cardDataChangedEvent);
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

}
