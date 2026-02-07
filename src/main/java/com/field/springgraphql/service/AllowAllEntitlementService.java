package com.field.springgraphql.service;

import org.springframework.stereotype.Service;

@Service
public class AllowAllEntitlementService implements EntitlementService {
  @Override
  public void assertUserEntitled(String userId) {
    // Intentionally allow all. Replace with real logic.
  }
}

