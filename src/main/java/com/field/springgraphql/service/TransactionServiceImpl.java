package com.field.springgraphql.service;

import com.field.springgraphql.graphql.TeamDto;
import com.field.springgraphql.graphql.TransactionDto;
import com.field.springgraphql.graphql.TransactionPageDto;
import com.field.springgraphql.graphql.TransactionSearchParamsInput;
import com.field.springgraphql.repository.TransactionSearchRepository;
import com.field.springgraphql.repository.TransactionSearchResult;
import com.field.springgraphql.repository.TransactionWithTeamsView;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {

  private final EntitlementService entitlementService;
  private final TransactionSearchRepository transactionSearchRepository;

  public TransactionServiceImpl(
      EntitlementService entitlementService,
      TransactionSearchRepository transactionSearchRepository
  ) {
    this.entitlementService = entitlementService;
    this.transactionSearchRepository = transactionSearchRepository;
  }

  @Override
  public TransactionPageDto searchTransactions(String userId, TransactionSearchParamsInput searchParams) {
    entitlementService.assertUserEntitled(userId);

    int page = searchParams.page() == null ? 0 : searchParams.page();
    int size = searchParams.size() == null ? 20 : searchParams.size();

    TransactionSearchResult<TransactionWithTeamsView> result =
        transactionSearchRepository.search(userId, searchParams);

    List<TransactionDto> items = result.items().stream().map(this::toDto).toList();
    long total = result.total();
    int totalPages = size <= 0 ? 0 : (int) Math.ceil((double) total / (double) size);

    return new TransactionPageDto(items, page, size, total, totalPages);
  }

  private TransactionDto toDto(TransactionWithTeamsView v) {
    List<TeamDto> teams = v.getTeams() == null ? List.of() : v.getTeams().stream()
        .map(t -> new TeamDto(t.getId() == null ? null : t.getId().toHexString(), t.getName()))
        .toList();

    String createdAt = v.getCreatedAt() == null ? null : v.getCreatedAt().toString();

    return new TransactionDto(
        v.getId() == null ? null : v.getId().toHexString(),
        v.getName(),
        v.getStatus(),
        v.getProjectName(),
        v.getProductName(),
        createdAt,
        teams
    );
  }
}

