package com.learn.graphql.config.security;

import static com.learn.graphql.config.security.GraphQLSecurityConfig.USER_ROLES_PRE_AUTH_HEADER;

import javax.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails;

public class GrantedAuthoritiesAuthenticationDetailsSource implements
    AuthenticationDetailsSource<HttpServletRequest, PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails> {

  @Override
  public PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails buildDetails(
      HttpServletRequest request) {
    var userRoles = request.getHeader(USER_ROLES_PRE_AUTH_HEADER);
    var authorities = GrantedAuthorityFactory.getAuthoritiesFrom(userRoles);
    return new PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails(request, authorities);
  }

}
