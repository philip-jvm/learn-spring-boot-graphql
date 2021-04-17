package com.learn.graphql.resolver.bank.subscription;

import com.learn.graphql.domain.bank.BankAccount;
import com.learn.graphql.publisher.BankAccountPublisher;
import graphql.kickstart.tools.GraphQLSubscriptionResolver;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Subscription (Chapter 33)
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BankAccountSubscription implements GraphQLSubscriptionResolver {

  private final BankAccountPublisher bankAccountPublisher;

  @PreAuthorize("hasAuthority('get:bank_account')")
  public Publisher<BankAccount> bankAccounts() {
    return bankAccountPublisher.getBankAccountPublisher();
  }

  @PreAuthorize("hasAuthority('get:bank_account')")
  public Publisher<BankAccount> bankAccount(UUID id) {
    // Access the user id, user roles etc
    var context = SecurityContextHolder.getContext();
    log.info("Creating bank account publisher for {}", context.getAuthentication().getPrincipal());

    return bankAccountPublisher.getBankAccountPublisherFor(id);
  }

}
