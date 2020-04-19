package com.github.zhengframework.jdbc;

import com.google.inject.Injector;
import com.google.inject.Key;
import java.lang.annotation.Annotation;
import java.util.concurrent.ExecutorService;
import javax.inject.Provider;
import javax.sql.DataSource;
import org.apache.commons.dbutils.AsyncQueryRunner;
import org.apache.commons.dbutils.QueryRunner;

public class AsyncQueryRunnerProvider implements Provider<AsyncQueryRunner> {

  private final Annotation qualifier;
  private Provider<Injector> injectorProvider;

  public AsyncQueryRunnerProvider(Annotation qualifier, Provider<Injector> injectorProvider) {
    this.qualifier = qualifier;
    this.injectorProvider = injectorProvider;
  }

  @Override
  public AsyncQueryRunner get() {
    DataSource dataSource;
    ExecutorService executorService;
    Injector injector = injectorProvider.get();
    if (qualifier == null) {
      dataSource = injector.getInstance(Key.get(DataSource.class));
      executorService = injector.getInstance(Key.get(ExecutorService.class));
    } else {
      dataSource = injector
          .getInstance(Key.get(DataSource.class, qualifier));
      executorService = injector.getInstance(Key.get(ExecutorService.class, qualifier));
    }
    return new AsyncQueryRunner(executorService, new QueryRunner(dataSource));
  }
}
