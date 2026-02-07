package com.field.springgraphql.graphql;

import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * Resolves exceptions in GraphQL data fetchers to user-friendly error messages.
 * Business exceptions (e.g. IllegalStateException with a message) expose their message;
 * other exceptions get a generic message and technical details are logged only.
 */
@Component
public class GraphQLExceptionResolver extends DataFetcherExceptionResolverAdapter {

  private static final Logger log = LoggerFactory.getLogger(GraphQLExceptionResolver.class);

  private static final String GENERIC_MESSAGE = "An unexpected error occurred. Please try again.";

  /** Exception types whose message is safe to show to the client. */
  private static final List<Class<? extends Throwable>> USER_FACING_EXCEPTIONS = List.of(
      IllegalStateException.class,
      IllegalArgumentException.class
  );

  @Override
  @Nullable
  protected graphql.GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
    UserFriendlyError result = getUserFriendlyError(ex);
    return GraphqlErrorBuilder.newError(env)
        .message(result.message())
        .errorType(result.errorType())
        .build();
  }

  private UserFriendlyError getUserFriendlyError(Throwable ex) {
    for (Class<? extends Throwable> type : USER_FACING_EXCEPTIONS) {
      if (type.isInstance(ex)) {
        String msg = ex.getMessage();
        if (msg != null && !msg.isBlank()) {
          return new UserFriendlyError(msg, ErrorType.BAD_REQUEST);
        }
      }
    }
    log.warn("GraphQL data fetcher error", ex);
    return new UserFriendlyError(GENERIC_MESSAGE, ErrorType.INTERNAL_ERROR);
  }

  private static record UserFriendlyError(String message, ErrorType errorType) {}
}
