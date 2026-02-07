package com.field.springgraphql.graphql;

import java.util.List;

public record TransactionDto(
    String id,
    String name,
    TransactionStatus status,
    String projectName,
    String productName,
    String createdAt,
    List<TeamDto> teams
) {
}

