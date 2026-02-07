package com.field.springgraphql.repository;

/**
 * Lightweight projection used for non-team-member search.
 * Only id, name, productName are populated; other Transaction fields are null in the response.
 */
public class NonTeamTransactionView {
  private String id;
  private String name;
  private String productName;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }
}

