package com.github.zhengframework.jdbc;

import java.util.List;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.sql.DataSource;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.spi.JdbiPlugin;

public class JdbiProvider implements Provider<Jdbi> {

  private final Provider<DataSource> dataSourceProvider;
  private final Provider<List<JdbiPlugin>> jdbiPluginListProvider;

  @Inject
  public JdbiProvider(Provider<DataSource> dataSourceProvider,
      Provider<List<JdbiPlugin>> jdbiPluginListProvider) {
    this.dataSourceProvider = dataSourceProvider;
    this.jdbiPluginListProvider = jdbiPluginListProvider;
  }

  @Override
  public Jdbi get() {
    List<JdbiPlugin> jdbiPluginList = jdbiPluginListProvider.get();
    Jdbi jdbi = Jdbi.create(dataSourceProvider.get());
    for (JdbiPlugin jdbiPlugin : jdbiPluginList) {
      jdbi.installPlugin(jdbiPlugin);
    }
    return jdbi;
  }
}
