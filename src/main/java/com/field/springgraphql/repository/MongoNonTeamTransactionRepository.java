package com.field.springgraphql.repository;

import com.field.springgraphql.graphql.TransactionSearchParamsInput;
import java.util.List;
import org.bson.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.FacetOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.count;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.facet;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.limit;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.skip;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;

@Repository
public class MongoNonTeamTransactionRepository implements NonTeamTransactionRepository {

  private final MongoTemplate mongoTemplate;

  public MongoNonTeamTransactionRepository(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public TransactionSearchResult<NonTeamTransactionView> searchByProjectNameStartsWith(TransactionSearchParamsInput params) {
    int page = params.page() == null ? 0 : params.page();
    int size = params.size() == null ? 20 : params.size();
    long skip = (long) page * size;

    String prefix = params.projectNameStartsWith();
    Criteria c = new Criteria();
    if (StringUtils.hasText(prefix)) {
      c = Criteria.where("projectName").regex("^" + escapeRegex(prefix.trim()) + ".*", "i");
    }

    List<AggregationOperation> pipeline = new java.util.ArrayList<>();
    if (c != null && c.getCriteriaObject() != null && !c.getCriteriaObject().isEmpty()) {
      pipeline.add(match(c));
    }

    ProjectionOperation projection = project("name", "productName");

    FacetOperation facet = facet(
        sort(Sort.by(Sort.Direction.DESC, "createdAt")),
        skip(skip),
        limit(size),
        projection
    ).as("items").and(
        count().as("total")
    ).as("meta");

    pipeline.add(facet);

    Aggregation agg = Aggregation.newAggregation(pipeline);
    AggregationResults<Document> results = mongoTemplate.aggregate(agg, "transactions", Document.class);
    Document root = results.getUniqueMappedResult();
    if (root == null) {
      return new TransactionSearchResult<>(List.of(), 0);
    }

    @SuppressWarnings("unchecked")
    List<Document> itemsDocs = (List<Document>) root.getOrDefault("items", List.of());
    List<NonTeamTransactionView> items = itemsDocs.stream().map(d -> {
      NonTeamTransactionView v = new NonTeamTransactionView();
      Object id = d.get("_id");
      if (id instanceof org.bson.types.ObjectId oid) {
        v.setId(oid.toHexString());
      }
      v.setName(d.getString("name"));
      v.setProductName(d.getString("productName"));
      return v;
    }).toList();

    long total = 0;
    @SuppressWarnings("unchecked")
    List<Document> meta = (List<Document>) root.getOrDefault("meta", List.of());
    if (!meta.isEmpty()) {
      Object t = meta.get(0).get("total");
      if (t instanceof Number n) {
        total = n.longValue();
      }
    }

    return new TransactionSearchResult<>(items, total);
  }

  private static String escapeRegex(String s) {
    return s.replace("\\", "\\\\")
        .replace(".", "\\.")
        .replace("*", "\\*")
        .replace("+", "\\+")
        .replace("?", "\\?")
        .replace("^", "\\^")
        .replace("$", "\\$")
        .replace("{", "\\{")
        .replace("}", "\\}")
        .replace("(", "\\(")
        .replace(")", "\\)")
        .replace("|", "\\|")
        .replace("[", "\\[")
        .replace("]", "\\]");
  }
}

