package com.easybank.common.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoanDataChangedEvent {

    private Long loanNumber;
    private String mobileNumber;
}
