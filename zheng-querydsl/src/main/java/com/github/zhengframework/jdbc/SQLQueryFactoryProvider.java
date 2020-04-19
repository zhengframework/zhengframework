package com.github.zhengframework.jdbc;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.querydsl.sql.Configuration;
import com.querydsl.sql.SQLQueryFactory;
import com.querydsl.sql.SQLTemplates;
import java.lang.annotation.Annotation;
import javax.inject.Provider;
import javax.sql.DataSource;

public class SQLQueryFactoryProvider implements Provider<SQLQueryFactory> {

  private final Annotation qualifier;
  private Provider<Injector> injectorProvider;

  public SQLQueryFactoryProvider(Annotation qualifier, Provider<Injector> injectorProvider) {
    this.qualifier = qualifier;
    this.injectorProvider = injectorProvider;
  }

  @Override
  public SQLQueryFactory get() {
    DataSource dataSource;
    SQLTemplates sqlTemplates;
    Injector injector = injectorProvider.get();
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
