package com.learn.graphql.resolver.bank.query;

import com.learn.graphql.connection.CursorUtil;
import com.learn.graphql.context.CustomGraphQLContext;
import com.learn.graphql.domain.bank.BankAccount;
import com.learn.graphql.domain.bank.Currency;
import com.learn.graphql.respository.BankAccountRepository;
import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.relay.Connection;
import graphql.relay.DefaultConnection;
import graphql.relay.DefaultEdge;
import graphql.relay.DefaultPageInfo;
import graphql.relay.Edge;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.SelectedField;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BankAccountQueryResolver implements GraphQLQueryResolver {

  private final BankAccountRepository bankAccountRepository;
  private final CursorUtil cursorUtil;
  private final Clock clock;

  @PreAuthorize("hasAuthority('get:bank_account')")
  public BankAccount bankAccount(UUID id, DataFetchingEnvironment environment) {
    log.info("Retrieving bank account id: {}", id);

    CustomGraphQLContext context = environment.getContext();

    log.info("User ID: {}", context.getUserId());

    var requestedFields = environment.getSelectionSet().getFields().stream()
        .map(SelectedField::getName).collect(Collectors.toUnmodifiableSet());

    log.info("Requested Fields: {}", requestedFields);

    return BankAccount.builder()
        .id(id)
        .currency(Currency.USD)
        .createdAt(ZonedDateTime.now(clock))
        .createdOn(LocalDate.now(clock))
        .build();
  }

  public Connection<BankAccount> bankAccounts(int first, @Nullable String cursor) {
    List<Edge<BankAccount>> edges = getBankAccounts(cursor)
        .stream()
        .map(bankAccount -> new DefaultEdge<>(bankAccount,
            cursorUtil.createCursorWith(bankAccount.getId())))
        .limit(first)
        .collect(Collectors.toUnmodifiableList());

    var pageInfo = new DefaultPageInfo(
        cursorUtil.getFirstCursorFrom(edges),
        cursorUtil.getLastCursorFrom(edges),
        cursor != null,
        edges.size() >= first);

    return new DefaultConnection<>(edges, pageInfo);
  }

  public List<BankAccount> getBankAccounts(String cursor) {
    if (cursor == null) {
      return bankAccountRepository.getBankAccounts();
    }
    return bankAccountRepository.getBankAccountsAfter(cursorUtil.decode(cursor));
  }

}
