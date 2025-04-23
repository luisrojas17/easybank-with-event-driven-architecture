package com.easybank.common.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountDataChangedEvent {

    private Long accountNumber;
    private String mobileNumber;
}
