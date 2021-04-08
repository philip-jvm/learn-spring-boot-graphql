package com.learn.graphql.publisher;

import com.learn.graphql.domain.bank.BankAccount;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.FluxProcessor;
import reactor.core.publisher.FluxSink;

/**
 * Subscription (Chapter 33)
 */
@Slf4j
@Component
public class BankAccountPublisher {

  /**
   * Use Sinks.Many with Reactor v3.4+
   */
  private final FluxProcessor<BankAccount, BankAccount> processor;
  private final FluxSink<BankAccount> sink;

  public BankAccountPublisher() {
    this.processor = DirectProcessor.<BankAccount>create().serialize();
    this.sink = processor.sink();
  }

  public void publish(BankAccount bankAccount) {
    sink.next(bankAccount);
  }

  public Publisher<BankAccount> getBankAccountPublisher() {
    return processor.map(bankAccount -> {
      log.info("Publishing global subscription update for Bank Account {}", bankAccount);
      return bankAccount;
    });
  }

  public Publisher<BankAccount> getBankAccountPublisherFor(UUID id) {
    return processor
        .filter(bankAccount -> id.equals(bankAccount.getId()))
        .map(bankAccount -> {
          log.info("Publishing individual subscription update for Bank Account {}", bankAccount);
          return bankAccount;
        });
  }

}
