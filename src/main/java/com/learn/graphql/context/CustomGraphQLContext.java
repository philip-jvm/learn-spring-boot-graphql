package com.learn.graphql.context;

import graphql.kickstart.servlet.context.GraphQLServletContext;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoaderRegistry;

/**
 * Custom Context (Chapter 22)
 */
@Getter
@RequiredArgsConstructor
public class CustomGraphQLContext implements GraphQLServletContext {

  private final String userId;
  private final GraphQLServletContext context;

  @Override
  public List<Part> getFileParts() {
    return context.getFileParts();
  }

  @Override
  public Map<String, List<Part>> getParts() {
    return context.getParts();
  }

  @Override
  public HttpServletRequest getHttpServletRequest() {
    return context.getHttpServletRequest();
  }

  @Override
  public HttpServletResponse getHttpServletResponse() {
    return context.getHttpServletResponse();
  }

  @Override
  public Optional<Subject> getSubject() {
    return context.getSubject();
  }

  @Override
  public DataLoaderRegistry getDataLoaderRegistry() {
    return context.getDataLoaderRegistry();
  }

}
