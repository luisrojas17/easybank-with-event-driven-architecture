package com.easybank.loans.function;

import com.easybank.common.dto.MobileNumberToUpdateDto;
import com.easybank.loans.service.LoanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Slf4j
@Configuration
public class LoanFunction {

    @Bean
    public Consumer<MobileNumberToUpdateDto> updateLoanMobileNumber(LoanService loanService) {
        return (mobileNumberToUpdateDto) -> {

            log.info("Receiving event to update loan mobile number [{}].",
                    mobileNumberToUpdateDto);

            var result = loanService.updateMobileNumber(mobileNumberToUpdateDto);
        };
    }

}
