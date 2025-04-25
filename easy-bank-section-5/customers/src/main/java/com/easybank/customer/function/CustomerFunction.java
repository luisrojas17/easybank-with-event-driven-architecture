package com.easybank.customer.function;

import com.easybank.common.dto.MobileNumberToUpdateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Slf4j
@Configuration
public class CustomerFunction {

    @Bean
    public Consumer<MobileNumberToUpdateDto> updateMobileNumberStatus() {

        return (mobileNumberToUpdateDto) -> {
            log.info("There were updated the mobile number [{}] in all Microservices' databases.",
                    mobileNumberToUpdateDto);
        };

    }
}
