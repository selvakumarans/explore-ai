# spring-graphql

Spring Boot microservice exposing a GraphQL endpoint to search transactions in MongoDB with a team join + `userId` entitlement filter.

## Data model (Mongo)

- `transactions` collection
  - `_id`: ObjectId
  - `name`: string
  - `status`: string (matches `TransactionStatus`)
  - `productName`: string
  - `createdAt`: date
  - `teamIds`: array of ObjectId (references `teams._id`)

- `teams` collection
  - `_id`: ObjectId
  - `name`: string
  - `userIds`: array of strings (users in the team)

The query joins `transactions.teamIds -> teams._id` and filters to transactions where **at least one joined team contains the given `userId`**.

## Run

1. Start MongoDB locally (default is `mongodb://localhost:27017/field`).
2. Run the app:

```bash
mvn spring-boot:run
```

3. Open GraphiQL at `http://localhost:8080/graphiql`.

## GraphQL example

```graphql
query {
  transactions(
    userId: "user-123",
    searchParams: { query: "alpha", page: 0, size: 10 }
  ) {
    page
    size
    totalElements
    totalPages
    items {
      id
      name
      status
      productName
      createdAt
      teams { id name }
    }
  }
}
```

## Entitlements

`com.field.springgraphql.service.AllowAllEntitlementService` currently allows all users.
Replace it with a real entitlement integration in `EntitlementService`.

