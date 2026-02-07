package com.field.springgraphql.repository;

import java.util.List;

public record TransactionSearchResult<T>(
    List<T> items,
    long total
) {
}

