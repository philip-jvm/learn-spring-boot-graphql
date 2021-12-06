package com.learn.graphql.util;

import static com.learn.graphql.instrumentation.RequestLoggingInstrumentation.CORRELATION_ID;

import java.util.UUID;
import java.util.concurrent.Executor;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.MDC;

@RequiredArgsConstructor(staticName = "wrap")
public class CorrelationIdPropagationExecutor implements Executor {

  private final Executor delegate;

  @Override
  public void execute(@NotNull Runnable command) {
    var correlationId = getCorrelationId();
    delegate.execute(() -> {
      try {
        MDC.put(CORRELATION_ID, correlationId);
        command.run();
      } finally {
        MDC.remove(CORRELATION_ID);
      }
    });
  }

  private String getCorrelationId() {
    // Assign a request correlation ID to new requests
    var correlationId = MDC.get(CORRELATION_ID);
    if (correlationId == null) {
      return UUID.randomUUID().toString();
    }
    return correlationId;
  }

}
