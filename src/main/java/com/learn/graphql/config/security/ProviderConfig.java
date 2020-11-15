package com.learn.graphql.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedGrantedAuthoritiesUserDetailsService;

@Configuration
public class ProviderConfig {

  @Bean
  public PreAuthenticatedAuthenticationProvider preAuthenticatedAuthenticationProvider() {
    var preAuthProvider = new PreAuthenticatedAuthenticationProvider();
    preAuthProvider.setPreAuthenticatedUserDetailsService(
        new PreAuthenticatedGrantedAuthoritiesUserDetailsService());
    return preAuthProvider;
  }

}
