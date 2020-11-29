package com.learn.graphql.config;

import graphql.validation.rules.OnValidationErrorStrategy;
import graphql.validation.rules.ValidationRules;
import graphql.validation.schemawiring.ValidationSchemaWiring;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Chapter 32: Schema Directive Validation
 */
@Configuration
public class ValidationDirectiveConfig {

  @Bean
  public ValidationSchemaWiring validationSchemaWiring() {
    // Contains the default validation @Directive constraints
    var validationRules = ValidationRules.newValidationRules()
        .onValidationErrorStrategy(OnValidationErrorStrategy.RETURN_NULL)
        .build();

    // Rewrites your data fetchers so that it's arguments are validated prior method execution
    return new ValidationSchemaWiring(validationRules);
  }

}
