package com.learn.graphql.resolver.bank.query;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

import com.graphql.spring.boot.test.GraphQLTestTemplate;
import io.micrometer.core.instrument.util.IOUtils;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = TestApplication.class)
public class BankAccountQueryResolverIT {

  private static final String GRAPHQL_QUERY_REQUEST_PATH = "graphql/resolver/query/request/%s.graphql";
  private static final String GRAPHQL_QUERY_RESPONSE_PATH = "graphql/resolver/query/response/%s.json";

  @Autowired
  GraphQLTestTemplate graphQLTestTemplate;

  @Test
  void bank_accounts_are_returned() throws IOException, JSONException {
    var testName = "bank_account";
    var graphQLResponse = graphQLTestTemplate
        .postForResource(format(GRAPHQL_QUERY_REQUEST_PATH, testName));

    var expectedResponseBody = read(format(GRAPHQL_QUERY_RESPONSE_PATH, testName));

    assertThat(graphQLResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertEquals(expectedResponseBody, graphQLResponse.getRawResponse().getBody(), true);
  }

  private String read(String location) throws IOException {
    return IOUtils.toString(
        new ClassPathResource(location).getInputStream(), StandardCharsets.UTF_8);
  }

}
