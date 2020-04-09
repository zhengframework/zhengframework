package com.dadazhishi.zheng.jdbc;

import com.google.inject.Injector;
import com.google.inject.Key;
import java.lang.annotation.Annotation;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.sql.DataSource;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

public class JdbiProvider implements Provider<Jdbi> {

  private final Annotation qualifier;

  @Inject
  private Injector injector;

  public JdbiProvider(Annotation qualifier) {
    this.qualifier = qualifier;
  }

  @Override
  public Jdbi get() {
    DataSource dataSource;
    if (qualifier == null) {
      dataSource = injector.getInstance(Key.get(DataSource.class));
    } else {
      dataSource = injector
          .getInstance(Key.get(DataSource.class, qualifier));
    }
    Jdbi jdbi = Jdbi.create(dataSource);
    jdbi.installPlugin(new SqlObjectPlugin());
    return jdbi;
  }
}
