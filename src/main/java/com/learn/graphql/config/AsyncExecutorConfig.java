package com.learn.graphql.config;

import com.learn.graphql.util.CorrelationIdPropagationExecutor;
import graphql.kickstart.autoconfigure.web.servlet.AsyncServletProperties;
import graphql.kickstart.autoconfigure.web.servlet.GraphQLServletProperties;
import java.util.concurrent.Executor;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;

/**
 * Override GraphQLWebSecurityAutoConfiguration async thread pool (graphqlAsyncTaskExecutor).
 */
@Configuration
@RequiredArgsConstructor
public class AsyncExecutorConfig {

  private final GraphQLServletProperties graphqlServletProperties;
  private final AsyncServletProperties asyncServletProperties;

  @Bean
  public Executor graphqlAsyncTaskExecutor() {
    var executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(10);
    executor.setMaxPoolSize(200);
    executor.setThreadNamePrefix("graphql-exec-");
    executor.initialize();
    /**
     * Assign and propagate the MDC correlation ID between threads.
     */
    return CorrelationIdPropagationExecutor.wrap(
        new DelegatingSecurityContextAsyncTaskExecutor(executor));
  }

  @PostConstruct
  public void validateAsyncModeActivated() {
    if (!isAsyncModeEnabled()) {
      throw new IllegalStateException("Application only supports async mode");
    }
  }

  private boolean isAsyncModeEnabled() {
    return graphqlServletProperties.getAsyncModeEnabled() != null
        ? graphqlServletProperties.getAsyncModeEnabled()
        : asyncServletProperties.isEnabled();
  }

}
