package com.field.springgraphql.graphql;

import graphql.schema.DataFetchingEnvironment;
import graphql.schema.SelectedField;
import java.util.List;
import java.util.Set;

/**
 * When nonTeamMember is true, only these Transaction fields are allowed in the response.
 */
public final class NonTeamMemberFieldValidator {

  /** Allowed Transaction fields for nonTeamMember: id, name, productName only. */
  public static final Set<String> ALLOWED_TRANSACTION_FIELDS = Set.of(
      "id",
      "name",
      "productName",
      "__typename"
  );

  private static final String ALLOWED_MESSAGE =
      "nonTeamMember search allows only these Transaction fields: id, name, productName. ";

  private NonTeamMemberFieldValidator() {}

  /**
   * Validates that the client only requested allowed fields on Transaction (under items).
   * @throws IllegalStateException with a clear message if any disallowed field is requested
   */
  public static void validateSelectionSet(DataFetchingEnvironment env) {
    var selectionSet = env.getSelectionSet();
    List<SelectedField> itemFields = selectionSet.getFields("items/*");
    for (SelectedField field : itemFields) {
      String name = field.getName();
      if (!ALLOWED_TRANSACTION_FIELDS.contains(name)) {
        throw new IllegalStateException(ALLOWED_MESSAGE + "Disallowed field requested: " + name);
      }
    }
  }
}
