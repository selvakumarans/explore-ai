package com.example.ai.graphql.input;

import graphql.schema.PropertyDataFetcher;
import graphql.schema.Coercing;
import graphql.schema.GraphQLScalarType;

public class PeopleSearch {
    private String peopleName;
    private String project;

    public PeopleSearch(String peopleName, String project) {
        this.peopleName = peopleName;
        this.project = project;
    }

    public String getPeopleName() {
        return peopleName;
    }

    public String getProject() {
        return project;
    }
}
