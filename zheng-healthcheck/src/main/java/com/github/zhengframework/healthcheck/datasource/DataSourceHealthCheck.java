package com.github.zhengframework.healthcheck.datasource;

import com.codahale.metrics.health.HealthCheck;
import com.github.zhengframework.guice.ClassScanner;
import com.google.inject.Injector;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.sql.DataSource;

public class DataSourceHealthCheck extends HealthCheck {

  private List<DataSource> dataSourceList;

  @Inject
  public DataSourceHealthCheck(
      Provider<Injector> injectorProvider) {
    Injector injector = injectorProvider.get();
    dataSourceList = new ArrayList<>();
    ClassScanner<DataSource> classScanner = new ClassScanner<>(injector, DataSource.class);
    classScanner.accept(thing -> dataSourceList.add(thing));
  }


  @Override
  protected Result check() {
    if (dataSourceList.isEmpty()) {
      return Result.healthy("dataSource not found");
    }
    for (DataSource dataSource : dataSourceList) {
      try (Connection connection = dataSource.getConnection()) {
        int timeout = 2;
        if (connection.isValid(timeout)) {
          return Result.healthy();
        } else {
          return Result.unhealthy("dataSource is invalid with timeout " + timeout + " second");
        }
      } catch (Exception e) {
        return Result.unhealthy(e.getMessage());
      }
    }
    return Result.healthy();
  }

}
