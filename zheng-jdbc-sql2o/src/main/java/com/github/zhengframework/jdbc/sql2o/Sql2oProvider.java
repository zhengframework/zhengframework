package com.github.zhengframework.jdbc.sql2o;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.sql.DataSource;
import org.sql2o.Sql2o;

public class Sql2oProvider implements Provider<Sql2o> {

  private Provider<DataSource> dataSourceProvider;

  @Inject
  public Sql2oProvider(Provider<DataSource> dataSourceProvider) {
    this.dataSourceProvider = dataSourceProvider;
  }

  @Override
  public Sql2o get() {
    return new Sql2o(dataSourceProvider.get());
  }
}
