package com.field.springgraphql.graphql;

import java.util.List;

public record TransactionPageDto(
    List<TransactionDto> items,
    int page,
    int size,
    long totalElements,
    int totalPages
) {
}

