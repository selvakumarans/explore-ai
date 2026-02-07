package com.example.ai.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface PeopleRepository extends MongoRepository<Person, String> {
    List<Person> findByNameContainingIgnoreCaseAndProjectContainingIgnoreCase(String name, String project);
}