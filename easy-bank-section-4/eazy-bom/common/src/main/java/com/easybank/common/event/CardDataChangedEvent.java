package com.easybank.common.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CardDataChangedEvent {

    private Long cardNumber;
    private String mobileNumber;
}
