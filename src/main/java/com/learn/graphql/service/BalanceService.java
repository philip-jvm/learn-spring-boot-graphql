package com.learn.graphql.service;

import com.learn.graphql.domain.bank.BankAccount;
import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BalanceService {

  public Map<UUID, BigDecimal> getBalanceFor(Map<UUID, BankAccount> bankAccountIds, String userId) {
    log.info("Requesting batch bank account ids: {} for user Id: {}", bankAccountIds, userId);

    // Spring security context is propagated to dataloader threads (current executing thread)
    log.info("User ID: {}", SecurityContextHolder.getContext().getAuthentication().getPrincipal());

    // Original set of ids is available via
    var ids = bankAccountIds.keySet();

    /**
     * Part 29: VisualVM JVM Profiling
     *   Set<BigDecimal> bigCrazy = new HashSet<>();
     *   var size = ThreadLocalRandom.current().nextInt(250, 500);
     *   var littleCrazy = new LinkedHashSet<BigDecimal>(size);
     *   IntStream.range(0, size).forEach(nextInt -> littleCrazy.add(BigDecimal.valueOf(nextInt)));
     *   bigCrazy.addAll(littleCrazy);
     */

    return Map.of(
        UUID.fromString("c6aa269a-812b-49d5-b178-a739a1ed74cc"), BigDecimal.ONE,
        UUID.fromString("48e4a484-af2c-4366-8cd4-25330597473f"), new BigDecimal("23431.22"));
  }
}
