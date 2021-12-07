package com.learn.graphql.config.security;

import static com.learn.graphql.config.security.GraphQLSecurityConfig.CORRELATION_ID;
import static com.learn.graphql.config.security.GraphQLSecurityConfig.USER_ROLES_PRE_AUTH_HEADER;

import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails;

public class GrantedAuthoritiesAuthenticationDetailsSource implements
    AuthenticationDetailsSource<HttpServletRequest, PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails> {

  @Override
  public PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails buildDetails(
      HttpServletRequest request) {
    var userRoles = request.getHeader(USER_ROLES_PRE_AUTH_HEADER);
    var authorities = GrantedAuthorityFactory.getAuthoritiesFrom(userRoles);
    MDC.put(CORRELATION_ID, UUID.randomUUID().toString());
    return new PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails(request, authorities);
  }

}
