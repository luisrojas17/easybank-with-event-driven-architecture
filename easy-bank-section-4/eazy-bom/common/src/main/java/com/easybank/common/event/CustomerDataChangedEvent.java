package com.easybank.common.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerDataChangedEvent {

    private String name;
    private String mobileNumber;
    private boolean activeSw;
}
