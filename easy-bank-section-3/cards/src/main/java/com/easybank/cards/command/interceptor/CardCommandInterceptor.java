package com.easybank.cards.command.interceptor;

import com.easybank.cards.command.CreateCardCommand;
import com.easybank.cards.command.DeleteCardCommand;
import com.easybank.cards.command.UpdateCardCommand;
import com.easybank.cards.constants.CardsConstants;
import com.easybank.cards.entity.CardEntity;
import com.easybank.cards.exception.CardAlreadyExistsException;
import com.easybank.cards.exception.ResourceNotFoundException;
import com.easybank.cards.repository.CardsRepository;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

@Component
@RequiredArgsConstructor
public class CardCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {

    private final CardsRepository cardsRepository;

    @Nonnull
    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(@Nonnull List<? extends CommandMessage<?>> messages) {
        return (index, command) -> {
            if (CreateCardCommand.class.equals(command.getPayloadType())) {
                CreateCardCommand createCardCommand = (CreateCardCommand) command.getPayload();
                Optional<CardEntity> optionalCards = cardsRepository.findByMobileNumberAndActiveSw(
                        createCardCommand.getMobileNumber(), true);
                if (optionalCards.isPresent()) {
                    throw new CardAlreadyExistsException("Card already created with given mobileNumber "
                            + createCardCommand.getMobileNumber());
                }
            } else if (UpdateCardCommand.class.equals(command.getPayloadType())) {
                UpdateCardCommand updateCardCommand = (UpdateCardCommand) command.getPayload();
                CardEntity card = cardsRepository.findByMobileNumberAndActiveSw
                        (updateCardCommand.getMobileNumber(), true).orElseThrow(() ->
                        new ResourceNotFoundException("Card", "mobileNumber", updateCardCommand.getMobileNumber()));
            } else if (DeleteCardCommand.class.equals(command.getPayloadType())) {
                DeleteCardCommand deleteCardCommand = (DeleteCardCommand) command.getPayload();
                CardEntity card = cardsRepository.findByCardNumberAndActiveSw(deleteCardCommand.getCardNumber(),
                        CardsConstants.ACTIVE_SW).orElseThrow(() -> new ResourceNotFoundException("Card", "cardNumber",
                        deleteCardCommand.getCardNumber().toString()));
            }
            return command;
        };
    }
}
