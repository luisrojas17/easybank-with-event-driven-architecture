package com.easybank.account.function;

import com.easybank.account.service.AccountService;
import com.easybank.common.dto.MobileNumberToUpdateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Slf4j
@Configuration
public class AccountFunction {

    @Bean
    public Consumer<MobileNumberToUpdateDto> updateAccountMobileNumber(AccountService accountService) {
        return (mobileNumberToUpdateDto) -> {

            log.info("Receiving event to update account mobile number [{}].",
                    mobileNumberToUpdateDto);

            accountService.updateMobileNumber(mobileNumberToUpdateDto);
        };
    }

}
