package com.github.zhengframework.jdbc;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.sql.DataSource;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;

public class DSLContextProvider implements Provider<DSLContext> {

  private Provider<DataSource> dataSourceProvider;
  private Provider<SQLDialect> sqlDialectProvider;
  private Provider<Settings> settingsProvider;

  @Inject
  public DSLContextProvider(Provider<DataSource> dataSourceProvider,
      Provider<SQLDialect> sqlDialectProvider, Provider<Settings> settingsProvider) {
    this.dataSourceProvider = dataSourceProvider;
    this.sqlDialectProvider = sqlDialectProvider;
    this.settingsProvider = settingsProvider;
  }

  @Override
  public DSLContext get() {
    return DSL.using(dataSourceProvider.get(), sqlDialectProvider.get(), settingsProvider.get());
  }
}
