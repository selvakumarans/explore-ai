package com.field.springgraphql.graphql;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record TransactionSearchParamsInput(
    String query,
    TransactionStatus status,
    String productName,
    String projectNameStartsWith,
    Boolean nonTeamMember,
    String fromDate,
    String toDate,
    @NotNull @Min(0) Integer page,
    @NotNull @Min(1) @Max(200) Integer size
) {
}

