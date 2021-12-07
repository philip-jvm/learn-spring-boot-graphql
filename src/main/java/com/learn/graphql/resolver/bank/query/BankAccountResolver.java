package com.learn.graphql.resolver.bank.query;

import com.learn.graphql.context.dataloader.DataLoaderRegistryFactory;
import com.learn.graphql.domain.bank.Asset;
import com.learn.graphql.domain.bank.BankAccount;
import com.learn.graphql.domain.bank.Client;
import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dataloader.DataLoader;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BankAccountResolver implements GraphQLResolver<BankAccount> {

  private final Executor bankAccountExecutor;

  public CompletableFuture<List<Asset>> assets(BankAccount bankAccount) {
    return CompletableFuture.supplyAsync(
        () -> {
          log.info("Getting assets for bank account id {}", bankAccount.getId());
          return List.of();
        },
        bankAccountExecutor);
  }

  public Client client(BankAccount bankAccount) {
    log.info("Requesting client data for bank account id {}", bankAccount.getId());
    return Client.builder()
        .id(UUID.randomUUID())
        .firstName("Philip")
        .lastName("Starritt")
        .build();
  }

  @PreAuthorize("hasAuthority('get:bank_account_balance')")
  public CompletableFuture<BigDecimal> balance(BankAccount bankAccount,
      DataFetchingEnvironment environment) {
    DataLoader<UUID, BigDecimal> dataLoader = environment
        .getDataLoader(DataLoaderRegistryFactory.BALANCE_DATA_LOADER);
    return dataLoader.load(bankAccount.getId(), bankAccount);
  }

}
