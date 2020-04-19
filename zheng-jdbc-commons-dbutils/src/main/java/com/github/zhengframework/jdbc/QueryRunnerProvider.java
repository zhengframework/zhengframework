package com.github.zhengframework.jdbc;

import com.google.inject.Injector;
import com.google.inject.Key;
import java.lang.annotation.Annotation;
import javax.inject.Provider;
import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;

public class QueryRunnerProvider implements Provider<QueryRunner> {

  private final Annotation qualifier;
  private Provider<Injector> injectorProvider;

  public QueryRunnerProvider(Annotation qualifier, Provider<Injector> injectorProvider) {
    this.qualifier = qualifier;
    this.injectorProvider = injectorProvider;
  }

  @Override
  public QueryRunner get() {
    DataSource dataSource;
    Injector injector = injectorProvider.get();
    if (qualifier == null) {
      dataSource = injector.getInstance(Key.get(DataSource.class));
    } else {
      dataSource = injector
          .getInstance(Key.get(DataSource.class, qualifier));
    }
    return new QueryRunner(dataSource);
  }
}
