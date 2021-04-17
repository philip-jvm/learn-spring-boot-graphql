package com.learn.graphql.config.security;

import graphql.kickstart.execution.subscriptions.SubscriptionSession;
import graphql.kickstart.execution.subscriptions.apollo.ApolloSubscriptionConnectionListener;
import graphql.kickstart.execution.subscriptions.apollo.OperationMessage;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthenticationConnectionListener implements ApolloSubscriptionConnectionListener {

  /**
   * Chapter 33: Subscriptions Spring Security Pre-Auth When using pre-auth, you must ensure that
   * all the graphql requests have been previously authorized/authenticated by an upstream service.
   * For example, all ingress traffic to this graphql server must bypass an upstream proxy node that
   * will validate the request's JWT token. This code alone performs no authorization. Read more
   * about Pre-auth before using this.
   */
  @Override
  public void onConnect(SubscriptionSession session, OperationMessage message) {
    log.info("onConnect with payload {}", message.getPayload());

    var payload = (Map<String, String>) message.getPayload();

    // Get the user id, roles (or JWT etc)
    var userId = payload.get(GraphQLSecurityConfig.USER_ID_PRE_AUTH_HEADER);
    var userRoles = payload.get(GraphQLSecurityConfig.USER_ROLES_PRE_AUTH_HEADER);
    var grantedAuthorities = GrantedAuthorityFactory.getAuthoritiesFrom(userRoles);

    var token = new PreAuthenticatedAuthenticationToken(userId, null, grantedAuthorities);
    SecurityContextHolder.getContext().setAuthentication(token);
  }

  @Override
  public void onStart(SubscriptionSession session, OperationMessage message) {
    log.info("onStart with payload {}", message.getPayload());
  }

  @Override
  public void onStop(SubscriptionSession session, OperationMessage message) {
    log.info("onStop with payload {}", message.getPayload());
    // Don't clear the context as this callback is executing on a different thread than onConnect/onStart
  }

  @Override
  public void onTerminate(SubscriptionSession session, OperationMessage message) {
    log.info("onTerminate with payload {}", message.getPayload());
  }

}
