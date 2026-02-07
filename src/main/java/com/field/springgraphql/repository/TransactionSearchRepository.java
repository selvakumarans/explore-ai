package com.field.springgraphql.repository;

import com.field.springgraphql.graphql.TransactionSearchParamsInput;

public interface TransactionSearchRepository {
  TransactionSearchResult<TransactionWithTeamsView> search(String userId, TransactionSearchParamsInput params);
}

