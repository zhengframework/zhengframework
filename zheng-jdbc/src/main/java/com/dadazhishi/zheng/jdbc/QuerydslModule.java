package com.github.zhengframework.jdbc;

import static com.google.inject.name.Names.named;

import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.querydsl.sql.SQLQueryFactory;
import java.lang.annotation.Annotation;
import java.util.Objects;
import lombok.EqualsAndHashCode;

/**
 * http://www.querydsl.com/static/querydsl/latest/reference/html/ch02s03.html
 */
@EqualsAndHashCode(callSuper = false, of = {"qualifier"})
public class QuerydslModule extends AbstractModule {

  private Annotation qualifier;

  public QuerydslModule(Annotation qualifier) {
    this.qualifier = qualifier;
  }

  public QuerydslModule(String name) {
    this.qualifier = named(Objects.requireNonNull(name));
  }


  @Override
  protected void configure() {
    SQLQueryFactoryProvider sqlQueryFactoryProvider = new SQLQueryFactoryProvider(qualifier);
    if (qualifier == null) {
      bind(Key.get(SQLQueryFactory.class)).toProvider(sqlQueryFactoryProvider);
    } else {
      bind(Key.get(SQLQueryFactory.class, qualifier)).toProvider(sqlQueryFactoryProvider);
    }
  }
}
