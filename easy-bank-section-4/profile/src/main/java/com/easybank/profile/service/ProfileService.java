package com.easybank.profile.service;

import com.easybank.common.event.AccountDataChangedEvent;
import com.easybank.common.event.CardDataChangedEvent;
import com.easybank.common.event.CustomerDataChangedEvent;
import com.easybank.common.event.LoanDataChangedEvent;
import com.easybank.profile.dto.ProfileDto;

public interface ProfileService {

    ProfileDto find(String mobileNumber);

    void handler(AccountDataChangedEvent accountDataChangedEvent);

    void handler(CardDataChangedEvent cardDataChangedEvent);

    void handler(CustomerDataChangedEvent customerDataChangedEvent);

    void handler(LoanDataChangedEvent loanDataChangedEvent);

}
