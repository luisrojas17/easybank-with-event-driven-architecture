package com.easybank.accounts.command.event;

import lombok.Data;

@Data
public class AccountDeletedEvent {

    private Long accountNumber;
    private boolean activeSw;

}
