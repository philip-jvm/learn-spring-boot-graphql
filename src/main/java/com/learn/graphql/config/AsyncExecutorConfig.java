package com.learn.graphql.config;

import com.learn.graphql.util.ExecutorFactory;
import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AsyncExecutorConfig {

  @Bean
  public Executor balanceExecutor(ExecutorFactory executorFactory) {
    return executorFactory.newExecutor();
  }

  @Bean
  public Executor bankAccountExecutor(ExecutorFactory executorFactory) {
    return executorFactory.newExecutor();
  }

}
