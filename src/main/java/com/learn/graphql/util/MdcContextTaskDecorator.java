package com.learn.graphql.util;

import graphql.kickstart.servlet.AsyncTaskDecorator;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;
import org.springframework.stereotype.Component;

@Component
public class MdcContextTaskDecorator implements AsyncTaskDecorator, TaskDecorator {

  /**
   * Propagate the current thread's MDC context to the target thread.
   */
  @Override
  public Runnable decorate(Runnable runnable) {
    var mdcContext = MDC.getCopyOfContextMap();
    return () -> {
      try {
        MDC.setContextMap(mdcContext);
        runnable.run();
      } finally {
        MDC.clear();
      }
    };
  }

}
