package com.learn.graphql.resolver.bank.mutation;

import com.learn.graphql.domain.bank.BankAccount;
import com.learn.graphql.domain.bank.Currency;
import com.learn.graphql.domain.bank.input.CreateBankAccountInput;
import com.learn.graphql.publisher.BankAccountPublisher;
import graphql.kickstart.tools.GraphQLMutationResolver;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Validated
@Component
@RequiredArgsConstructor
public class BankAccountMutation implements GraphQLMutationResolver {

  private final Clock clock;
  private final BankAccountPublisher bankAccountPublisher;

  /**
   * JSR-303 Bean Validation (Chapter 18)
   */
  public BankAccount createBankAccount(@Valid CreateBankAccountInput input) {
    log.info("Creating bank account for {}", input);
    return getBankAccount(UUID.randomUUID());
  }

  /**
   * Schema Directive Validation (Chapter 32)
   */
  public BankAccount updateBankAccount(UUID id, String name, int age) {
    log.info("Updating bank account for {}. Name: {}, age: {}", id, name, age);
    return getBankAccount(id);
  }

  private BankAccount getBankAccount(UUID id) {
    var bankAccount = BankAccount.builder()
        .id(id)
        .currency(Currency.USD)
        .createdAt(ZonedDateTime.now(clock))
        .createdOn(LocalDate.now(clock))
        .build();

    /**
     * Subscription (Chapter 33)
     */
    bankAccountPublisher.publish(bankAccount);

    return bankAccount;
  }

}
