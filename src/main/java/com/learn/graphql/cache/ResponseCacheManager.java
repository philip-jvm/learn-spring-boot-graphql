package com.learn.graphql.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.learn.graphql.config.security.GraphQLSecurityConfig;
import graphql.kickstart.execution.input.GraphQLInvocationInput;
import graphql.kickstart.servlet.cache.CachedResponse;
import graphql.kickstart.servlet.cache.GraphQLResponseCacheManager;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Response Query Caching: Chapter 35
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ResponseCacheManager implements GraphQLResponseCacheManager {

  private final Cache<RequestKey, CachedResponse> responseCache;

  @Override
  public CachedResponse get(HttpServletRequest request, GraphQLInvocationInput invocationInput) {
    return responseCache.getIfPresent(getRequestKey(request, invocationInput));
  }

  @Override
  public boolean isCacheable(HttpServletRequest request, GraphQLInvocationInput invocationInput) {
    // Do not cache introspection query
    return invocationInput.getQueries()
        .stream()
        .noneMatch(this::isIntrospectionQuery);
  }

  @Override
  public void put(HttpServletRequest request, GraphQLInvocationInput invocationInput,
      CachedResponse cachedResponse) {
    responseCache.put(getRequestKey(request, invocationInput), cachedResponse);
  }

  private RequestKey getRequestKey(HttpServletRequest request,
      GraphQLInvocationInput invocationInput) {
    return new RequestKey(getUserId(request), invocationInput.getQueries());
  }

  private String getUserId(HttpServletRequest request) {
    var userId = request.getHeader(GraphQLSecurityConfig.USER_ID_PRE_AUTH_HEADER);
    if (userId == null) {
      throw new IllegalArgumentException("User Id is null. Cannot read from ResponseCacheManager.");
    }
    return userId;
  }

  private boolean isIntrospectionQuery(String query) {
    return query.contains("Introspection");
  }

}
