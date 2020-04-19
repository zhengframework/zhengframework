package com.github.zhengframework.jdbc;

import static com.google.inject.name.Names.named;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Key;
import java.lang.annotation.Annotation;
import java.util.Objects;
import lombok.EqualsAndHashCode;
import org.apache.commons.dbutils.QueryRunner;

@EqualsAndHashCode(callSuper = false)
public class CommonsDBUtilsModule extends AbstractModule {

  private Annotation qualifier;

  public CommonsDBUtilsModule(Annotation qualifier) {
    this.qualifier = qualifier;
  }

  public CommonsDBUtilsModule(String name) {
    this.qualifier = named(Objects.requireNonNull(name));
  }

  public CommonsDBUtilsModule() {
    qualifier = null;
  }

  @Override
  protected void configure() {
    QueryRunnerProvider queryRunnerProvider = new QueryRunnerProvider(qualifier,
        getProvider(Injector.class));
    requestInjection(queryRunnerProvider);
    if (qualifier == null) {
      bind(Key.get(QueryRunner.class)).toProvider(queryRunnerProvider);
    } else {
      bind(Key.get(QueryRunner.class, qualifier)).toProvider(queryRunnerProvider);
    }
  }
}
