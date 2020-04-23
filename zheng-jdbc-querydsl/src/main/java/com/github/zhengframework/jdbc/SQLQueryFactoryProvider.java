package com.github.zhengframework.jdbc;

import com.querydsl.sql.Configuration;
import com.querydsl.sql.SQLQueryFactory;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.sql.DataSource;

public class SQLQueryFactoryProvider implements Provider<SQLQueryFactory> {

  private Provider<DataSource> dataSourceProvider;
  private Provider<Configuration> configurationProvider;

  @Inject
  public SQLQueryFactoryProvider(Provider<DataSource> dataSourceProvider,
      Provider<Configuration> configurationProvider) {
    this.dataSourceProvider = dataSourceProvider;
    this.configurationProvider = configurationProvider;
  }

  @Override
  public SQLQueryFactory get() {
    return new SQLQueryFactory(configurationProvider.get(), dataSourceProvider.get());
  }
}
