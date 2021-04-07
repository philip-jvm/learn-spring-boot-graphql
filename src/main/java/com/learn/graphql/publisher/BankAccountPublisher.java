package com.learn.graphql.publisher;

import com.learn.graphql.domain.bank.BankAccount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxProcessor;
import reactor.core.publisher.FluxSink;

/**
 * Subscription (Chapter 33)
 */
@Slf4j
@Component
public class BankAccountPublisher {

  private final FluxProcessor<BankAccount, BankAccount> processor;
  private final FluxSink<BankAccount> sink;

  public BankAccountPublisher() {
    this.processor = DirectProcessor.<BankAccount>create().serialize();
    this.sink = processor.sink();
  }

  public void publish(BankAccount bankAccount) {
    sink.next(bankAccount);
  }

  public Flux<BankAccount> getBankAccountPublisher() {
    return processor.map(bankAccount -> {
      log.info("Publishing Bank Account {}", bankAccount);
      return bankAccount;
    });
  }

}
