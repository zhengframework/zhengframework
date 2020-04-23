package com.github.zhengframework.jdbc.migrate;

import static org.junit.Assert.assertTrue;

import com.github.zhengframework.bootstrap.ZhengApplication;
import com.github.zhengframework.bootstrap.ZhengApplicationBuilder;
import com.google.inject.Injector;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.sql.DataSource;
import org.junit.Test;

public class LiquibaseMigrateModuleTest {

  @Test
  public void configure() throws Exception {
    ZhengApplication application = ZhengApplicationBuilder.create().addModule(
        new LiquibaseMigrateModule())
        .enableAutoLoadModule()
        .build();
    application.start();
    Injector injector = application.getInjector();
    DataSource dataSource = injector.getInstance(DataSource.class);
    Connection connection = dataSource.getConnection();
    DatabaseMetaData metaData = connection.getMetaData();
    System.out.println(metaData.getDatabaseProductName());
    Statement statement = connection.createStatement();
    ResultSet resultSet = statement.executeQuery("SELECT * FROM public.department;");
    assertTrue(resultSet.next());
    resultSet.close();
    statement.close();
    connection.close();
    application.stop();
  }
}