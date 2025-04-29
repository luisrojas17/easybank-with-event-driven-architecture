package com.easybank.loan.command.interceptor;

import com.easybank.loan.command.CreateLoanCommand;
import com.easybank.loan.command.DeleteLoanCommand;
import com.easybank.loan.command.UpdateLoanCommand;
import com.easybank.loan.entity.LoanEntity;
import com.easybank.loan.exception.LoanAlreadyExistsException;
import com.easybank.loan.exception.ResourceNotFoundException;
import com.easybank.loan.repository.LoanRepository;
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
public class LoanCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {

    private final LoanRepository loanRepository;

    @Nonnull
    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(
            @Nonnull List<? extends CommandMessage<?>> messages) {

        return (index, command) -> {

            // To Create loan command
            if (CreateLoanCommand.class.equals(command.getPayloadType())) {
                CreateLoanCommand createLoanCommand = (CreateLoanCommand) command.getPayload();
                Optional<LoanEntity> optionalLoanEntity = loanRepository.findByMobileNumberAndActiveSw(
                        createLoanCommand.getMobileNumber(), true);

                // Loan already exists
                if (optionalLoanEntity.isPresent()) {
                    throw new LoanAlreadyExistsException("Loan already created with given mobile number "
                            + createLoanCommand.getMobileNumber());
                }

            }
            // To Update loan command
            else if (UpdateLoanCommand.class.equals(command.getPayloadType())) {
                UpdateLoanCommand updateLoanCommand = (UpdateLoanCommand) command.getPayload();
                LoanEntity loanEntity = loanRepository.findByMobileNumberAndActiveSw(
                        updateLoanCommand.getMobileNumber(), true)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Loan", "mobileNumber",
                                        updateLoanCommand.getMobileNumber()));

            }
            // To Delete loan command
            else if (DeleteLoanCommand.class.equals(command.getPayloadType())) {
                DeleteLoanCommand deleteLoanCommand = (DeleteLoanCommand) command.getPayload();
                LoanEntity loanEntity = loanRepository.findByLoanNumberAndActiveSw(
                        deleteLoanCommand.getLoanNumber(), true)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Loan", "loanNumber",
                                        deleteLoanCommand.getLoanNumber().toString()));
            }

            return command;
        };
    }
}
