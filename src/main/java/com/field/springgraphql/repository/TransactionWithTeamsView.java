package com.field.springgraphql.repository;

import com.field.springgraphql.graphql.TransactionStatus;
import java.time.Instant;
import java.util.List;
import org.bson.types.ObjectId;

/**
 * Mongo aggregation projection for a transaction with joined team docs.
 */
public class TransactionWithTeamsView {
  private ObjectId id;
  private String name;
  private TransactionStatus status;
  private String projectName;
  private String productName;
  private Instant createdAt;
  private List<TeamView> teams;

  public ObjectId getId() {
    return id;
  }

  public void setId(ObjectId id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public TransactionStatus getStatus() {
    return status;
  }

  public void setStatus(TransactionStatus status) {
    this.status = status;
  }

  public String getProjectName() {
    return projectName;
  }

  public void setProjectName(String projectName) {
    this.projectName = projectName;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }

  public List<TeamView> getTeams() {
    return teams;
  }

  public void setTeams(List<TeamView> teams) {
    this.teams = teams;
  }

  public static class TeamView {
    private ObjectId id;
    private String name;

    public ObjectId getId() {
      return id;
    }

    public void setId(ObjectId id) {
      this.id = id;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }
}

