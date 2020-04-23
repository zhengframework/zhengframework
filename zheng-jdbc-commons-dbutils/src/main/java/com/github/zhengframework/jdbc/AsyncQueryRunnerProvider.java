package com.github.zhengframework.jdbc;

import java.util.concurrent.ExecutorService;
import javax.inject.Provider;
import javax.sql.DataSource;
import org.apache.commons.dbutils.AsyncQueryRunner;
import org.apache.commons.dbutils.QueryRunner;

public class AsyncQueryRunnerProvider implements Provider<AsyncQueryRunner> {

  private final Provider<DataSource> dataSourceProvider;
  private final Provider<ExecutorService> executorServiceProvider;

  public AsyncQueryRunnerProvider(Provider<DataSource> dataSourceProvider,
      Provider<ExecutorService> executorServiceProvider) {
    this.dataSourceProvider = dataSourceProvider;
    this.executorServiceProvider = executorServiceProvider;
  }

  @Override
  public AsyncQueryRunner get() {
    return new AsyncQueryRunner(executorServiceProvider.get(),
        new QueryRunner(dataSourceProvider.get()));
  }
}
