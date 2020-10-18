package com.learn.graphql.resolver.bank.query;

import static java.time.ZoneOffset.UTC;

import com.learn.graphql.Application;
import java.time.Clock;
import java.time.LocalDate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

@Configuration
@Import(Application.class)
public class TestApplication {

  @Bean
  @Primary
  public Clock testClock() {
    return Clock.fixed(LocalDate.of(2020, 10, 16).atStartOfDay(UTC).toInstant(), UTC);
  }

}
