package com.learn.graphql.config.security;

import javax.servlet.Filter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;

@Slf4j
@Configuration
@EnableWebSecurity // Debug = true, will print the execution of the FilterChainProxy
@EnableGlobalMethodSecurity(
    securedEnabled = true,
    prePostEnabled = true)
@RequiredArgsConstructor
public class GraphQLSecurityConfig extends WebSecurityConfigurerAdapter {

  /**
   * Chapter 31: Spring Security Pre-Auth
   * When using pre-auth, you must ensure that all the graphql requests have been previously
   * authorized/authenticated by an upstream service.
   * For example, all ingress traffic to this graphql server must bypass an upstream proxy node that will validate
   * the request's JWT token. This code alone performs no authorization. Read more about Pre-auth before using this.
   */

  /**
   * Using pre-auth headers provide you the ability to switch or support other authentication
   * methods without making any/many application code changes. (E.g. JWT to something else)
   */
  public static final String USER_ID_PRE_AUTH_HEADER = "user_id";
  public static final String USER_ROLES_PRE_AUTH_HEADER = "user_roles";
  public static final String CORRELATION_ID = "correlation_id";

  private final PreAuthenticatedAuthenticationProvider preAuthenticatedAuthenticationProvider;

  @Override
  public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) {
    authenticationManagerBuilder.authenticationProvider(preAuthenticatedAuthenticationProvider);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    log.info("Configuring spring security");

    http
        // Add the Pre Authentication Filter
        .addFilterBefore(createRequestHeadersPreAuthenticationFilter(),
            AbstractPreAuthenticatedProcessingFilter.class)
        .authorizeRequests()
        // All endpoints require authentication
        .anyRequest().authenticated()
        .and()
        // Disable CSRF Token generation
        .csrf().disable()
        // Disable the default HTTP Basic-Auth
        .httpBasic().disable()
        // Disable the session management filter
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        // Disable the /logout filter
        .logout().disable()
        // Disable anonymous users
        .anonymous().disable();
  }

  @Override
  public void configure(WebSecurity web) {
    web.ignoring()
        // Actuator health endpoint for readiness, liveness checks etc
        .antMatchers("/actuator/health")
        // Permit playground for development
        .antMatchers("/playground", "/vendor/playground/**")
        // Subscription are secured via AuthenticationConnectionListener
        .antMatchers("/subscriptions");
  }

  private Filter createRequestHeadersPreAuthenticationFilter() throws Exception {
    var filter = new RequestHeadersPreAuthenticationFilter();
    filter.setAuthenticationDetailsSource(new GrantedAuthoritiesAuthenticationDetailsSource());
    filter.setAuthenticationManager(authenticationManager());
    filter.setContinueFilterChainOnUnsuccessfulAuthentication(false);
    return filter;
  }

}
