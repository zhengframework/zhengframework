package com.github.zhengframework.jdbc;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import java.lang.annotation.Annotation;
import java.util.List;
import javax.inject.Provider;
import javax.sql.DataSource;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.spi.JdbiPlugin;

public class JdbiProvider implements Provider<Jdbi> {

  private final Annotation qualifier;
  private Provider<Injector> injectorProvider;

  public JdbiProvider(Annotation qualifier, Provider<Injector> injectorProvider) {
    this.qualifier = qualifier;
    this.injectorProvider = injectorProvider;
  }

  @Override
  public Jdbi get() {
    DataSource dataSource;
    List<JdbiPlugin> jdbiPluginList;
    Injector injector = injectorProvider.get();
    if (qualifier == null) {
      dataSource = injector.getInstance(Key.get(DataSource.class));
      jdbiPluginList = injector.getInstance(Key.get(new TypeLiteral<List<JdbiPlugin>>() {
      }));
    } else {
      dataSource = injector
          .getInstance(Key.get(DataSource.class, qualifier));
      jdbiPluginList = injector.getInstance(Key.get(new TypeLiteral<List<JdbiPlugin>>() {
      }, qualifier));
    }
    Jdbi jdbi = Jdbi.create(dataSource);
    for (JdbiPlugin jdbiPlugin : jdbiPluginList) {
      jdbi.installPlugin(jdbiPlugin);
    }
    return jdbi;
  }
}
