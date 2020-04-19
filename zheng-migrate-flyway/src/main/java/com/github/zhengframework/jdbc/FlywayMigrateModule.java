package com.github.zhengframework.jdbc;

import com.google.inject.AbstractModule;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
public class FlywayMigrateModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(ManagedSchema.class).to(FlywayManagedSchema.class);
  }
}