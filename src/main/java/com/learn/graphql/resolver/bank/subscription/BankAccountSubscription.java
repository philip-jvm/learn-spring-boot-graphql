package com.learn.graphql.resolver.bank.subscription;

import com.learn.graphql.config.security.AuthenticationConnectionListener;
import com.learn.graphql.domain.bank.BankAccount;
import com.learn.graphql.publisher.BankAccountPublisher;
import graphql.kickstart.servlet.context.GraphQLWebSocketContext;
import graphql.kickstart.tools.GraphQLSubscriptionResolver;
import graphql.schema.DataFetchingEnvironment;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
  public Publisher<BankAccount> bankAccount(UUID id, DataFetchingEnvironment e) {
    log.info("Creating bank account publisher for user Id: {}",
        SecurityContextHolder.getContext().getAuthentication().getPrincipal());

    // As an alternative to spring-security, you can access the authentication via the DataFetchingEnvironment
    GraphQLWebSocketContext context = getContext(e);
    var authentication = (Authentication) context.getSession().getUserProperties()
        .get(AuthenticationConnectionListener.AUTHENTICATION);
    log.info("Creating bank account publisher for user Id: {}",
        authentication.getPrincipal());

    return bankAccountPublisher.getBankAccountPublisherFor(id);
  }

  private <T> T getContext(DataFetchingEnvironment e) {
    return e.getContext();
  }

}
