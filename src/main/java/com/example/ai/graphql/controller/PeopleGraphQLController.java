package com.example.ai.graphql.controller;

import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Controller;
import java.util.List;

@Controller
public class PeopleGraphQLController implements GraphQLQueryResolver {

    public List<String> searchPeople(String name) {
        // Implementation goes here
        return List.of("Person 1", "Person 2"); // Example response
    }
}