package com.field.springgraphql.service;

import com.field.springgraphql.graphql.TransactionDto;
import com.field.springgraphql.graphql.TransactionPageDto;
import com.field.springgraphql.graphql.TransactionSearchParamsInput;
import com.field.springgraphql.repository.NonTeamTransactionRepository;
import com.field.springgraphql.repository.NonTeamTransactionView;
import com.field.springgraphql.repository.TransactionSearchResult;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class NonTeamTransactionServiceImpl implements NonTeamTransactionService {

  private final NonTeamTransactionRepository nonTeamTransactionRepository;

  public NonTeamTransactionServiceImpl(NonTeamTransactionRepository nonTeamTransactionRepository) {
    this.nonTeamTransactionRepository = nonTeamTransactionRepository;
  }

  @Override
  public TransactionPageDto searchNonTeamTransactions(TransactionSearchParamsInput searchParams) {
    int page = searchParams.page() == null ? 0 : searchParams.page();
    int size = searchParams.size() == null ? 20 : searchParams.size();

    TransactionSearchResult<NonTeamTransactionView> result =
        nonTeamTransactionRepository.searchByProjectNameStartsWith(searchParams);

    // Only id, name, productName are populated; other fields are null/empty for nonTeamMember.
    List<TransactionDto> items = result.items().stream().map(v ->
        new TransactionDto(
            v.getId(),
            v.getName(),
            null,
            null,
            v.getProductName(),
            null,
            List.of()
        )
    ).toList();

    long total = result.total();
    int totalPages = size <= 0 ? 0 : (int) Math.ceil((double) total / (double) size);

    return new TransactionPageDto(items, page, size, total, totalPages);
  }
}

