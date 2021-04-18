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
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequestLoggingInstrumentation extends SimpleInstrumentation {

  public static final String CORRELATION_ID = "correlation_id";

  private final Clock clock;

  @Override
  public InstrumentationContext<ExecutionResult> beginExecution(
      InstrumentationExecutionParameters parameters) {
    var start = Instant.now(clock);
    // Add the correlation ID to the NIO thread
    MDC.put(CORRELATION_ID, parameters.getExecutionInput().getExecutionId().toString());

    log.info("Query: {} with variables: {}", parameters.getQuery(), parameters.getVariables());
    return SimpleInstrumentationContext.whenCompleted((executionResult, throwable) -> {
      var duration = Duration.between(start, Instant.now(clock));
      if (throwable == null) {
        log.info("Completed successfully in: {}", duration);
      } else {
        log.warn("Failed in: {}", duration, throwable);
      }
      // If we have async resolvers, this callback can occur in the thread-pool and not the NIO thread.
      // In that case, the LoggingListener will be used as a fallback to clear the NIO thread.
      MDC.clear();
    });
  }

}
