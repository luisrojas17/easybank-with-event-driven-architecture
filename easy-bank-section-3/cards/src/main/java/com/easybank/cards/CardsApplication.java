package com.easybank.cards;

import com.easybank.cards.command.interceptor.CardCommandInterceptor;
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
public class CardsApplication {

    public static void main(String[] args) {
        SpringApplication.run(CardsApplication.class, args);
    }

    @Autowired
    public void registerCardCommandInterceptor(
            ApplicationContext applicationContext, CommandGateway commandGateway) {

        commandGateway.registerDispatchInterceptor(
                applicationContext.getBean(CardCommandInterceptor.class));

    }

    @Autowired
    public void eventProcessingConfigurer(EventProcessingConfigurer eventProcessingConfigurer) {
        eventProcessingConfigurer.registerListenerInvocationErrorHandler(
                "card-group", conf -> PropagatingErrorHandler.instance());
    }
}
