package com.easybank.account;

import com.easybank.account.command.interceptor.AccountCommandInterceptor;
import com.easybank.common.config.AxonConfig;
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
public class AccountApplication {

    public static void main(String[] args) {

        SpringApplication.run(AccountApplication.class, args);
    }

    @Autowired
    public void registerAccountCommandInterceptor(ApplicationContext context, CommandGateway commandGateway) {
        commandGateway.registerDispatchInterceptor(context.getBean(AccountCommandInterceptor.class));
    }

    @Autowired
    public void configure(EventProcessingConfigurer eventProcessingConfigurer) {
        eventProcessingConfigurer.registerListenerInvocationErrorHandler("account-group",
                config -> PropagatingErrorHandler.instance());
    }

}
