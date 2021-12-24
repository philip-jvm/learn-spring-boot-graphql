package com.learn.graphql.context.dataloader;

import com.learn.graphql.service.BalanceService;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import lombok.RequiredArgsConstructor;
import org.dataloader.BatchLoaderEnvironment;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoaderRegistryFactory {

  public static final String BALANCE_DATA_LOADER = "BALANCE_DATA_LOADER";

  private final BalanceService balanceService;
  private final Executor balanceExecutor;

  public DataLoaderRegistry create(String userId) {
    var registry = new DataLoaderRegistry();
    registry.register(BALANCE_DATA_LOADER, createBalanceDataLoader(userId));
    return registry;
  }

  private DataLoader<UUID, BigDecimal> createBalanceDataLoader(String userId) {
    return DataLoader
        .newMappedDataLoader((Set<UUID> bankAccountIds, BatchLoaderEnvironment environment) ->
            CompletableFuture.supplyAsync(() ->
                    balanceService.getBalanceFor((Map) environment.getKeyContexts(), userId),
                balanceExecutor));
  }

}
