package com.learn.graphql.config.security;

import static com.learn.graphql.config.security.GraphQLSecurityConfig.CORRELATION_ID;

import graphql.kickstart.execution.subscriptions.SubscriptionSession;
import graphql.kickstart.execution.subscriptions.apollo.ApolloSubscriptionConnectionListener;
import graphql.kickstart.execution.subscriptions.apollo.OperationMessage;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthenticationConnectionListener implements ApolloSubscriptionConnectionListener {

  public static final String AUTHENTICATION = "AUTHENTICATION";

  /**
   * Chapter 34: Subscriptions Spring Security Pre-Auth. When using pre-auth, you must ensure that
   * all the graphql requests have been previously authorized/authenticated by an upstream service.
   * For example, all ingress traffic to this graphql server must bypass an upstream proxy node that
   * will validate the request's JWT token. This code alone performs no authorization. Read more
   * about Pre-auth before using this.
   */
  @Override
  public void onConnect(SubscriptionSession session, OperationMessage message) {
    log.info("onConnect with payload {}", message.getPayload());

    var payload = (Map<String, String>) message.getPayload();

    // Get the user id, roles (or JWT etc) and perform authentication / rejection here
    var userId = payload.get(GraphQLSecurityConfig.USER_ID_PRE_AUTH_HEADER);
    var userRoles = payload.get(GraphQLSecurityConfig.USER_ROLES_PRE_AUTH_HEADER);
    var grantedAuthorities = GrantedAuthorityFactory.getAuthoritiesFrom(userRoles);

    /**

     Q: Why do not set the token/Authentication inside Spring Security SecurityContextHolder here?

     If the start frame is not sent directly with the connection_init then the two frames may be serviced on different threads.
     The thread servicing the connection_init frame will check the websocket for any further inbound frames,
     if false the thread will move onto another websocket. Another thread is then free to service the following start frame.
     In this case, that thread not have the security context of the correct session/thread.

     Same scenario happens for onStop. (Message can be executed on different thread).

     This seems to be why some users are reporting intermittent failures with spring security.
     E.g. https://github.com/graphql-java-kickstart/graphql-java-servlet/discussions/134#discussioncomment-225980

     With the NIO connector, a small number of threads will check sessions for new frames.
     If a session has a frame available, the session will be passed to another thread pool which will read frame, execute it, check for another frame, execute it (loop).
     The session will be released when there are no further frames available. With this, we know that at most one thread will concurrently access one socket,
     therefore frames will be read sequentially. We can therefore extract the auth credentials from onConnect and add them to the session.getUserProperties().
     These properties are available in the onStart and onStop callbacks. Inside these callbacks, we can add the token to the SecurityContextHolder if we decide to use method level security,
     or simply access the credentials inside the subscription resolver via DataFetchingEnvironment.

     */

    var token = new PreAuthenticatedAuthenticationToken(userId, null, grantedAuthorities);
    session.getUserProperties().put(AUTHENTICATION, token);
    session.getUserProperties().put(CORRELATION_ID, UUID.randomUUID().toString());
  }

  @Override
  public void onStart(SubscriptionSession session, OperationMessage message) {
    log.info("onStart with payload {}", message.getPayload());
    var authentication = (Authentication) session.getUserProperties().get(AUTHENTICATION);
    SecurityContextHolder.getContext().setAuthentication(authentication);
    MDC.put(CORRELATION_ID, (String) session.getUserProperties().get(CORRELATION_ID));
  }

  @Override
  public void onStop(SubscriptionSession session, OperationMessage message) {
    log.info("onStop with payload {}", message.getPayload());
  }

  @Override
  public void onTerminate(SubscriptionSession session, OperationMessage message) {
    log.info("onTerminate with payload {}", message.getPayload());
    MDC.clear();
  }

}
