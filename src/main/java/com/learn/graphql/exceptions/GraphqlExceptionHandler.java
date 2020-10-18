package com.learn.graphql.exceptions;

import graphql.GraphQLException;
import graphql.kickstart.spring.error.ThrowableGraphQLError;
import javax.validation.ConstraintViolationException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Component
public class GraphqlExceptionHandler {

  @ExceptionHandler({GraphQLException.class, ConstraintViolationException.class})
  public ThrowableGraphQLError handle(Exception e) {
    return new ThrowableGraphQLError(e);
  }

  @ExceptionHandler(RuntimeException.class)
  public ThrowableGraphQLError handle(RuntimeException e) {
    return new ThrowableGraphQLError(e, "Internal Server Error");
  }

}
