package com.field.springgraphql.graphql;

import com.field.springgraphql.service.NonTeamTransactionService;
import com.field.springgraphql.service.TransactionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

@Controller
@Validated
public class TransactionQueryController {

  private final TransactionService transactionService;
  private final NonTeamTransactionService nonTeamTransactionService;

  public TransactionQueryController(
      TransactionService transactionService,
      NonTeamTransactionService nonTeamTransactionService
  ) {
    this.transactionService = transactionService;
    this.nonTeamTransactionService = nonTeamTransactionService;
  }

  /**
   * When nonTeamMember is true: no entitlement check; only id, projectName, productName are
   * populated; other Transaction fields (name, status, createdAt, teams) are null/empty.
   */
  @QueryMapping
  public TransactionPageDto transactions(
      @Argument @NotBlank String userId,
      @Argument @Valid TransactionSearchParamsInput searchParams
  ) {
    if (Boolean.TRUE.equals(searchParams.nonTeamMember())) {
      return nonTeamTransactionService.searchNonTeamTransactions(searchParams);
    }
    return transactionService.searchTransactions(userId, searchParams);
  }
}

