package com.easybank.accounts.command.interceptor;

import com.easybank.accounts.command.CreateAccountCommand;
import com.easybank.accounts.command.DeleteAccountCommand;
import com.easybank.accounts.command.UpdateAccountCommand;
import com.easybank.accounts.constants.AccountsConstants;
import com.easybank.accounts.entity.AccountEntity;
import com.easybank.accounts.exception.AccountAlreadyExistsException;
import com.easybank.accounts.exception.ResourceNotFoundException;
import com.easybank.accounts.repository.AccountRepository;
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
public class AccountCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {

    private final AccountRepository accountRepository;

    @Nonnull
    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(
            @Nonnull List<? extends CommandMessage<?>> messages) {

        return (index, command) -> {

            if (CreateAccountCommand.class.equals(command.getPayloadType())) {
                CreateAccountCommand createAccountCommand = (CreateAccountCommand) command.getPayload();
                Optional<AccountEntity> optionalAccounts =
                        accountRepository.findByMobileNumberAndActiveSw(
                                createAccountCommand.getMobileNumber(), AccountsConstants.ACTIVE_SW);

                if (optionalAccounts.isPresent()) {
                    throw new AccountAlreadyExistsException(
                            String.format("Account already created with given mobileNumber: %s", createAccountCommand.getMobileNumber()));
                }
            } else if (UpdateAccountCommand.class.equals(command.getPayloadType())) {
                UpdateAccountCommand updateAccountCommand = (UpdateAccountCommand) command.getPayload();
                AccountEntity accountEntity =
                        accountRepository.findByMobileNumberAndActiveSw(
                                updateAccountCommand.getMobileNumber(), AccountsConstants.ACTIVE_SW)
                                .orElseThrow(() ->
                                        new ResourceNotFoundException("Account", "mobileNumber", updateAccountCommand.getMobileNumber())
                        );
            } else if (DeleteAccountCommand.class.equals(command.getPayloadType())) {
                DeleteAccountCommand deleteAccountCommand = (DeleteAccountCommand) command.getPayload();
                AccountEntity accountEntity =
                        accountRepository.findByAccountNumberAndActiveSw(
                                deleteAccountCommand.getAccountNumber(), AccountsConstants.ACTIVE_SW)
                                .orElseThrow(() ->
                                        new ResourceNotFoundException("Account", "accountNumber", deleteAccountCommand.getAccountNumber().toString())
                        );

            }

            return command;
        };

    }
}
