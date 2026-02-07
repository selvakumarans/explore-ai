package com.field.springgraphql.service;

/**
 * Replace this with your real entitlement integration (e.g. call IAM/OPA, fetch roles, etc).
 */
public interface EntitlementService {
  void assertUserEntitled(String userId);
}

