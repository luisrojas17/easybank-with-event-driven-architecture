package com.easybank.loans;

import com.easybank.loans.command.interceptor.LoanCommandInterceptor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.config.EventProcessingConfigurer;
import org.axonframework.eventhandling.PropagatingErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
public class LoansApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoansApplication.class, args);
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
