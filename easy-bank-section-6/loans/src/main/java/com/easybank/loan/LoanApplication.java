package com.easybank.loan;

import com.easybank.common.config.AxonConfig;
import com.easybank.loan.command.interceptor.LoanCommandInterceptor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.config.EventProcessingConfigurer;
import org.axonframework.eventhandling.PropagatingErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@Import(AxonConfig.class)
public class LoanApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoanApplication.class, args);
    }

    @Autowired
    public void registerLoanCommandInterceptor(ApplicationContext applicationContext, CommandGateway commandGateway) {
        commandGateway.registerDispatchInterceptor(applicationContext.getBean(LoanCommandInterceptor.class));
    }

    @Autowired
    public void eventProcessingConfigurer(EventProcessingConfigurer eventProcessingConfigurer) {
        eventProcessingConfigurer.registerListenerInvocationErrorHandler("loan-group",
                conf -> PropagatingErrorHandler.instance());
    }
}
