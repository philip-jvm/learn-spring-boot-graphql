package com.learn.graphql.resolver.bank.subscription;

import com.learn.graphql.domain.bank.BankAccount;
import com.learn.graphql.publisher.BankAccountPublisher;
import graphql.kickstart.tools.GraphQLSubscriptionResolver;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;

/**
 * Subscription (Chapter 33)
 */
@Component
@RequiredArgsConstructor
public class BankAccountSubscription implements GraphQLSubscriptionResolver {

  private final BankAccountPublisher bankAccountPublisher;

  public Publisher<BankAccount> bankAccounts() {
    return bankAccountPublisher.getBankAccountPublisher();
  }

}
