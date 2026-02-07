package com.field.springgraphql.service;

import com.field.springgraphql.graphql.TransactionPageDto;
import com.field.springgraphql.graphql.TransactionSearchParamsInput;

/**
 * For non-team-member searches: no entitlement check, no team join.
 */
public interface NonTeamTransactionService {
  TransactionPageDto searchNonTeamTransactions(TransactionSearchParamsInput searchParams);
}

