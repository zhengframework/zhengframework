package com.github.zhengframework.jdbc;

import com.github.zhengframework.bootstrap.ZhengApplication;
import com.github.zhengframework.bootstrap.ZhengApplicationBuilder;
import com.google.inject.Injector;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import javax.sql.DataSource;

public class FlywayMigrateModuleTest {

  @org.junit.Test
  public void configure() throws Exception {
    ZhengApplication application = ZhengApplicationBuilder.create().addModule(
        new FlywayMigrateModule())
        .enableAutoLoadModule()
//        .withConfiguration(configuration)
        .build();
    application.start();
    Injector injector = application.getInjector();
    DataSource dataSource = injector.getInstance(DataSource.class);
    Connection connection = dataSource.getConnection();
    DatabaseMetaData metaData = connection.getMetaData();
    System.out.println(metaData.getDatabaseProductName());
    connection.close();
    application.stop();
  }
}