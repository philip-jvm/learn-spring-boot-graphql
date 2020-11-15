package com.learn.graphql.config.security;

import static com.learn.graphql.config.security.GraphQLSecurityConfig.USER_ID_PRE_AUTH_HEADER;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

public class RequestHeadersPreAuthenticationFilter extends
    AbstractPreAuthenticatedProcessingFilter {

  @Override
  protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
    return request.getHeader(USER_ID_PRE_AUTH_HEADER);
  }

  @Override
  protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
    return StringUtils.EMPTY;
  }

}
