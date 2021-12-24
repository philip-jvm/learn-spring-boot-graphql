package com.learn.graphql.listener;

import graphql.kickstart.servlet.core.GraphQLServletListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingListener implements GraphQLServletListener {

  /**
   * Override other default methods to provide GraphQL life-cycle callbacks.
   */
  private static final RequestCallback ON_FINALLY_LISTENER = new RequestCallback() {
    @Override
    public void onFinally(HttpServletRequest request, HttpServletResponse response) {
      // The final callback in the GraphQL life-cycle
      log.info("OnFinally: GraphQL query complete");
    }
  };

  @Override
  public RequestCallback onRequest(HttpServletRequest request, HttpServletResponse response) {
    return ON_FINALLY_LISTENER;
  }

}
