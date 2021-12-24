package com.learn.graphql.util;

import java.util.concurrent.Executor;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExecutorFactory {

  private final TaskDecorator mdcContextTaskDecorator;

  public Executor newExecutor() {
    var executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(Runtime.getRuntime().availableProcessors());
    executor.setMaxPoolSize(Runtime.getRuntime().availableProcessors());
    executor.setKeepAliveSeconds(0);
    executor.setTaskDecorator(mdcContextTaskDecorator);
    executor.initialize();
    return new DelegatingSecurityContextAsyncTaskExecutor(executor);
  }

}
