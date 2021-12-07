package com.learn.graphql.instrumentation;

import graphql.ExecutionResult;
import graphql.execution.instrumentation.InstrumentationContext;
import graphql.execution.instrumentation.SimpleInstrumentation;
import graphql.execution.instrumentation.SimpleInstrumentationContext;
import graphql.execution.instrumentation.parameters.InstrumentationExecutionParameters;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequestLoggingInstrumentation extends SimpleInstrumentation {

  private final Clock clock;

  @Override
  public InstrumentationContext<ExecutionResult> beginExecution(
      InstrumentationExecutionParameters parameters) {
    var start = Instant.now(clock);
    log.info("Query: {} with variables: {}", parameters.getQuery(), parameters.getVariables());
    return SimpleInstrumentationContext.whenCompleted((executionResult, throwable) -> {
      // This callback will occur in the resolver thread.

      var duration = Duration.between(start, Instant.now(clock));
      if (throwable == null) {
        log.info("Completed successfully in: {}", duration);
      } else {
        log.warn("Failed in: {}", duration, throwable);
      }
    });
  }

}
