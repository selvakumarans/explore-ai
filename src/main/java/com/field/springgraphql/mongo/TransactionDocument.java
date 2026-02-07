package com.field.springgraphql.mongo;

import com.field.springgraphql.graphql.TransactionStatus;
import java.time.Instant;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("transactions")
public class TransactionDocument {
  @Id
  private ObjectId id;

  private String name;
  private TransactionStatus status;
  private String projectName;
  private String productName;
  private Instant createdAt;

  /**
   * Teams that the transaction belongs to.
   * Used to lookup {@code teams} collection via {@code _id}.
   */
  private List<ObjectId> teamIds;

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

  public List<ObjectId> getTeamIds() {
    return teamIds;
  }

  public void setTeamIds(List<ObjectId> teamIds) {
    this.teamIds = teamIds;
  }
}

