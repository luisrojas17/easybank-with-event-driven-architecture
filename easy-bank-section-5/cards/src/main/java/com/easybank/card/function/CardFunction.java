package com.easybank.card.function;

import com.easybank.card.service.CardService;
import com.easybank.common.dto.MobileNumberToUpdateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Slf4j
@Configuration
public class CardFunction {

    @Bean
    public Consumer<MobileNumberToUpdateDto> updateCardMobileNumber(CardService cardService) {
        return (mobileNumberToUpdateDto) -> {

            log.info("Receiving event to update card mobile number [{}].",
                    mobileNumberToUpdateDto);

            cardService.updateMobileNumber(mobileNumberToUpdateDto);
        };
    }
}
