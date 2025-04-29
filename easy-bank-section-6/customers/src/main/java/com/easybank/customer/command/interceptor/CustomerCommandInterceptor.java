package com.easybank.customer.command.interceptor;

import com.easybank.customer.command.CreateCustomerCommand;
import com.easybank.customer.command.DeleteCustomerCommand;
import com.easybank.customer.command.UpdateCustomerCommand;
import com.easybank.customer.constants.CustomerConstants;
import com.easybank.customer.entity.CustomerEntity;
import com.easybank.customer.exception.CustomerAlreadyExistsException;
import com.easybank.customer.exception.ResourceNotFoundException;
import com.easybank.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

@Component
@RequiredArgsConstructor
public class CustomerCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {

    private final CustomerRepository customerRepository;

    @Nonnull
    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(@Nonnull List<? extends CommandMessage<?>> messages) {
        return (index, command) -> {
            if(CreateCustomerCommand.class.equals(command.getPayloadType())) {
                CreateCustomerCommand createCustomerCommand = (CreateCustomerCommand) command.getPayload();

                Optional<CustomerEntity> optionalCustomer =
                        customerRepository.findByMobileNumberAndActiveSw(createCustomerCommand.getMobileNumber(), true);

                if (optionalCustomer.isPresent()) {
                    throw new CustomerAlreadyExistsException("Customer already registered with given mobileNumber "
                            + createCustomerCommand.getMobileNumber());
                }
            } else if(UpdateCustomerCommand.class.equals(command.getPayloadType())) {
                UpdateCustomerCommand updateCustomerCommand = (UpdateCustomerCommand) command.getPayload();

                CustomerEntity customer =
                        customerRepository.findByMobileNumberAndActiveSw(updateCustomerCommand.getMobileNumber(), true).orElseThrow(() ->
                                new ResourceNotFoundException("Customer", "mobileNumber",
                                        updateCustomerCommand.getMobileNumber()));

            } else if(DeleteCustomerCommand.class.equals(command.getPayloadType())) {
                DeleteCustomerCommand deleteCustomerCommand = (DeleteCustomerCommand) command.getPayload();

                CustomerEntity customer =
                        customerRepository.findByCustomerIdAndActiveSw(deleteCustomerCommand.getCustomerId(),
                        CustomerConstants.ACTIVE_SW).orElseThrow(
                                () -> new ResourceNotFoundException("Customer", "customerId",
                                        deleteCustomerCommand.getCustomerId()));
            }
            return command;
        };
    }
}
