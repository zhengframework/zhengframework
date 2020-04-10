package com.github.zhengframework.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.inject.Inject;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

@Provider
public class ObjectMapperContextResolver implements ContextResolver<ObjectMapper> {

  private final ObjectMapper objectMapper;

  @Inject
  public ObjectMapperContextResolver(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public ObjectMapper getContext(Class<?> type) {
    return objectMapper;
  }
}
