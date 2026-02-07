package com.field.springgraphql.service;

import com.field.springgraphql.graphql.TransactionPageDto;
import com.field.springgraphql.graphql.TransactionSearchParamsInput;

public interface TransactionService {
  TransactionPageDto searchTransactions(String userId, TransactionSearchParamsInput searchParams);
}

