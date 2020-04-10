package com.github.zhengframework.jdbc;

import com.google.inject.Injector;
import com.google.inject.Key;
import java.lang.annotation.Annotation;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.sql.DataSource;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

public class DSLContextProvider implements Provider<DSLContext> {

  private final Annotation qualifier;

  @Inject
  private SQLDialect sqlDialect;

  @Inject
  private Injector injector;

  public DSLContextProvider(Annotation qualifier) {
    this.qualifier = qualifier;
  }


  @Override
  public DSLContext get() {
    DataSource dataSource;
    if (qualifier == null) {
      dataSource = injector.getInstance(Key.get(DataSource.class));
    } else {
      dataSource = injector
          .getInstance(Key.get(DataSource.class, qualifier));
    }
    return DSL.using(dataSource, sqlDialect);
  }
}
