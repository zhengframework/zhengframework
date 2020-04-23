package com.github.zhengframework.jdbc;

import static com.google.inject.name.Names.named;
import static org.junit.Assert.assertNotNull;

import com.github.zhengframework.bootstrap.ZhengApplication;
import com.github.zhengframework.bootstrap.ZhengApplicationBuilder;
import com.github.zhengframework.configuration.Configuration;
import com.github.zhengframework.configuration.ConfigurationBuilder;
import com.github.zhengframework.configuration.source.FileConfigurationSource;
import com.google.inject.Injector;
import com.google.inject.Key;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.junit.Assert;
import org.junit.Test;
import org.sql2o.Sql2o;

public class Sql2oModuleTest {

  @Test
  public void configure() throws SQLException {
    Configuration configuration = new ConfigurationBuilder()
        .withConfigurationSource(new FileConfigurationSource("application.properties"))
        .build();
    System.out.println(configuration.asMap());

    ZhengApplication application = ZhengApplicationBuilder.create()
        .enableAutoLoadModule()
        .withConfiguration(configuration)
        .build();
    DataSource dataSource = application.getInjector().getInstance(DataSource.class);
    Connection connection = dataSource.getConnection();
    DatabaseMetaData metaData = connection.getMetaData();
    System.out.println(metaData.getDatabaseProductName());
    Assert.assertEquals("HSQL Database Engine", metaData.getDatabaseProductName());
    Sql2o instance = application.getInjector().getInstance(Sql2o.class);
    assertNotNull(instance);
  }

  @Test
  public void configureGroup() throws SQLException {
    Configuration configuration = new ConfigurationBuilder()
        .withConfigurationSource(new FileConfigurationSource("application_group.properties"))
        .build();
    ZhengApplication application = ZhengApplicationBuilder.create()
        .enableAutoLoadModule()
        .withConfiguration(configuration)
        .build();
    Injector injector = application.getInjector();
    DataSource dataSourceA = injector
        .getInstance(Key.get(DataSource.class, named("a")));
    DatabaseMetaData metaDataA = dataSourceA.getConnection().getMetaData();
    System.out.println(metaDataA.getDatabaseProductName());
    Assert.assertEquals("HSQL Database Engine", metaDataA.getDatabaseProductName());

    DataSource dataSourceB = injector
        .getInstance(Key.get(DataSource.class, named("b")));
    DatabaseMetaData metaDataB = dataSourceB.getConnection().getMetaData();
    Assert.assertEquals(metaDataB.getDatabaseProductName(), metaDataA.getDatabaseProductName());
    Assert.assertNotEquals(dataSourceA, dataSourceB);
    assertNotNull(injector.getInstance(Key.get(Sql2o.class, named("a"))));
    assertNotNull(injector.getInstance(Key.get(Sql2o.class, named("b"))));

  }
}