package com.github.zhengframework.jdbc;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.querydsl.sql.Configuration;
import com.querydsl.sql.SQLQueryFactory;
import com.querydsl.sql.SQLTemplates;
import java.lang.annotation.Annotation;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.sql.DataSource;

public class SQLQueryFactoryProvider implements Provider<SQLQueryFactory> {

  private final Annotation qualifier;

  @Inject
  private Injector injector;

  public SQLQueryFactoryProvider(Annotation qualifier) {
    this.qualifier = qualifier;
  }


  @Override
  public SQLQueryFactory get() {
    DataSource dataSource;
    SQLTemplates sqlTemplates;
    if (qualifier == null) {
      dataSource = injector.getInstance(Key.get(DataSource.class));
      sqlTemplates = injector.getInstance(Key.get(SQLTemplates.class));
    } else {
      dataSource = injector
          .getInstance(Key.get(DataSource.class, qualifier));
      sqlTemplates = injector.getInstance(Key.get(SQLTemplates.class, qualifier));
    }
    Configuration configuration = new Configuration(sqlTemplates);
    return new SQLQueryFactory(configuration, dataSource);
  }
}
