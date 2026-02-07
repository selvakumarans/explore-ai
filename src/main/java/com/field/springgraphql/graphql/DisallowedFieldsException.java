package com.field.springgraphql.graphql;

/**
 * Thrown when nonTeamMember is true but the client requested Transaction fields
 * other than id, name, productName. The message is returned to the client in the GraphQL errors array.
 */
public final class DisallowedFieldsException extends RuntimeException {

  public static final String ERROR_CODE = "DISALLOWED_FIELDS";

  private final String disallowedField;

  public DisallowedFieldsException(String message, String disallowedField) {
    super(message);
    this.disallowedField = disallowedField;
  }

  public String getDisallowedField() {
    return disallowedField;
  }
}
