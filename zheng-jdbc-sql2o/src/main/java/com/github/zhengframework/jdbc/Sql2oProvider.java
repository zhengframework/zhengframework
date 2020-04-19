package com.github.zhengframework.jdbc;

import com.google.inject.Injector;
import com.google.inject.Key;
import java.lang.annotation.Annotation;
import javax.inject.Provider;
import javax.sql.DataSource;
import org.sql2o.Sql2o;

public class Sql2oProvider implements Provider<Sql2o> {

  private final Annotation qualifier;
  private Provider<Injector> injectorProvider;

  public Sql2oProvider(Annotation qualifier, Provider<Injector> injectorProvider) {
    this.qualifier = qualifier;
    this.injectorProvider = injectorProvider;
  }

  @Override
  public Sql2o get() {
    DataSource dataSource;

    Injector injector = injectorProvider.get();
    if (qualifier == null) {
      dataSource = injector.getInstance(Key.get(DataSource.class));
    } else {
      dataSource = injector
          .getInstance(Key.get(DataSource.class, qualifier));
    }
    return new Sql2o(dataSource);
  }
}
