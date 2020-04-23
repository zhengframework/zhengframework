package com.github.zhengframework.jdbc;

import javax.inject.Provider;
import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;

public class QueryRunnerProvider implements Provider<QueryRunner> {

  private final Provider<DataSource> dataSourceProvider;

  public QueryRunnerProvider(Provider<DataSource> dataSourceProvider) {
    this.dataSourceProvider = dataSourceProvider;
  }


  @Override
  public QueryRunner get() {
    return new QueryRunner(dataSourceProvider.get());
  }
}
