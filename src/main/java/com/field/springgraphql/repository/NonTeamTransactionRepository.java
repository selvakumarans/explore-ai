package com.field.springgraphql.repository;

import com.field.springgraphql.graphql.TransactionSearchParamsInput;

public interface NonTeamTransactionRepository {
  TransactionSearchResult<NonTeamTransactionView> searchByProjectNameStartsWith(TransactionSearchParamsInput params);
}

