package com.field.springgraphql.repository;

import com.field.springgraphql.graphql.TransactionSearchParamsInput;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.FacetOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.count;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.facet;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.skip;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.limit;

@Repository
public class MongoTransactionSearchRepository implements TransactionSearchRepository {

  private final MongoTemplate mongoTemplate;

  public MongoTransactionSearchRepository(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public TransactionSearchResult<TransactionWithTeamsView> search(String userId, TransactionSearchParamsInput params) {
    int page = params.page() == null ? 0 : params.page();
    int size = params.size() == null ? 20 : params.size();
    long skip = (long) page * size;

    List<AggregationOperation> pipeline = new ArrayList<>();

    Criteria txCriteria = new Criteria();
    List<Criteria> and = new ArrayList<>();

    if (params.status() != null) {
      and.add(Criteria.where("status").is(params.status()));
    }
    if (StringUtils.hasText(params.productName())) {
      and.add(Criteria.where("productName").regex(".*" + escapeRegex(params.productName().trim()) + ".*", "i"));
    }
    if (StringUtils.hasText(params.query())) {
      String q = escapeRegex(params.query().trim());
      and.add(new Criteria().orOperator(
          Criteria.where("name").regex(".*" + q + ".*", "i"),
          Criteria.where("productName").regex(".*" + q + ".*", "i")
      ));
    }
    Instant from = parseDateLowerBound(params.fromDate());
    Instant to = parseDateUpperBound(params.toDate());
    if (from != null || to != null) {
      Criteria c = Criteria.where("createdAt");
      if (from != null) c = c.gte(from);
      if (to != null) c = c.lte(to);
      and.add(c);
    }
    if (!and.isEmpty()) {
      txCriteria = new Criteria().andOperator(and);
      pipeline.add(match(txCriteria));
    }

    // Join teams, then filter to only those where userId is a member of at least one joined team.
    LookupOperation lookupTeams = LookupOperation.newLookup()
        .from("teams")
        .localField("teamIds")
        .foreignField("_id")
        .as("teams");
    pipeline.add(lookupTeams);
    pipeline.add(match(Criteria.where("teams.userIds").is(userId)));

    ProjectionOperation projection = project("name", "status", "projectName", "productName", "createdAt")
        .and("teams").as("teams");

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
    List<TransactionWithTeamsView> items = itemsDocs.stream()
        .map(this::mapView)
        .toList();

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

  private TransactionWithTeamsView mapView(Document d) {
    TransactionWithTeamsView v = new TransactionWithTeamsView();
    Object id = d.get("_id");
    if (id instanceof org.bson.types.ObjectId oid) {
      v.setId(oid);
    }
    v.setName(d.getString("name"));
    Object status = d.get("status");
    if (status instanceof String s) {
      try {
        v.setStatus(com.field.springgraphql.graphql.TransactionStatus.valueOf(s));
      } catch (IllegalArgumentException ignored) {
        v.setStatus(null);
      }
    }
    v.setProductName(d.getString("productName"));
    v.setProjectName(d.getString("projectName"));
    Object createdAt = d.get("createdAt");
    if (createdAt instanceof java.util.Date dt) {
      v.setCreatedAt(dt.toInstant());
    }

    @SuppressWarnings("unchecked")
    List<Document> teams = (List<Document>) d.getOrDefault("teams", List.of());
    List<TransactionWithTeamsView.TeamView> teamViews = teams.stream().map(td -> {
      TransactionWithTeamsView.TeamView tv = new TransactionWithTeamsView.TeamView();
      Object tid = td.get("_id");
      if (tid instanceof org.bson.types.ObjectId oid) {
        tv.setId(oid);
      }
      tv.setName(td.getString("name"));
      return tv;
    }).toList();
    v.setTeams(teamViews);
    return v;
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

  private static Instant parseDateLowerBound(String s) {
    if (!StringUtils.hasText(s)) return null;
    String v = s.trim();
    try {
      return Instant.parse(v);
    } catch (DateTimeParseException ignored) {
    }
    try {
      return OffsetDateTime.parse(v).toInstant();
    } catch (DateTimeParseException ignored) {
    }
    try {
      return LocalDate.parse(v).atStartOfDay().toInstant(ZoneOffset.UTC);
    } catch (DateTimeParseException ignored) {
    }
    return null;
  }

  private static Instant parseDateUpperBound(String s) {
    if (!StringUtils.hasText(s)) return null;
    String v = s.trim();
    try {
      return Instant.parse(v);
    } catch (DateTimeParseException ignored) {
    }
    try {
      return OffsetDateTime.parse(v).toInstant();
    } catch (DateTimeParseException ignored) {
    }
    try {
      // Inclusive end-of-day UTC for yyyy-MM-dd inputs
      return LocalDate.parse(v).plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC).minusMillis(1);
    } catch (DateTimeParseException ignored) {
    }
    return null;
  }
}

