package com.dadazhishi.zheng.jdbc;

import static com.google.inject.name.Names.named;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Provides;
import java.util.Objects;
import javax.inject.Provider;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

@Slf4j
public class JdbiModule extends AbstractModule {

  private final String name;

  public JdbiModule(String name) {
    this.name = name;
  }

  public JdbiModule() {
    name = null;
  }

  @Provides
  public Jdbi jdbi(Provider<Injector> injectorProvider) {
    DataSource dataSource;
    if (name == null) {
      dataSource = injectorProvider.get().getInstance(Key.get(DataSource.class));
    } else {
      dataSource = injectorProvider.get()
          .getInstance(Key.get(DataSource.class, named(name)));
    }
    Jdbi jdbi = Jdbi.create(dataSource);
    jdbi.installPlugin(new SqlObjectPlugin());
    return jdbi;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    JdbiModule that = (JdbiModule) o;
    return Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }
}
