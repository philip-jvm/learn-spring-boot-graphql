package com.learn.graphql.util;

import java.util.concurrent.Executor;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExecutorFactory {

  public static Executor newExecutor() {
    var executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(Runtime.getRuntime().availableProcessors());
    executor.setMaxPoolSize(Runtime.getRuntime().availableProcessors());
    executor.setKeepAliveSeconds(0);
    executor.initialize();
    executor.setTaskDecorator(new MdcContextTaskDecorator());
    return new DelegatingSecurityContextAsyncTaskExecutor(executor);
  }

}
