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
import javax.sql.DataSource;
import org.apache.commons.dbutils.AsyncQueryRunner;
import org.apache.commons.dbutils.QueryRunner;
import org.junit.Assert;
import org.junit.Test;

public class CommonsDBUtilsModuleTest {

  @Test
  public void configure() throws Exception {
    Configuration configuration = new ConfigurationBuilder()
        .withConfigurationSource(new FileConfigurationSource("application.properties"))
        .build();
    System.out.println(configuration.asMap());

    ZhengApplication application = ZhengApplicationBuilder.create()
        .enableAutoLoadModule()
        .withConfiguration(configuration)
        .build();
    application.start();
    Injector injector = application.getInjector();
    DataSource dataSource = injector.getInstance(DataSource.class);
    Connection connection = dataSource.getConnection();
    DatabaseMetaData metaData = connection.getMetaData();
    System.out.println(metaData.getDatabaseProductName());
    Assert.assertEquals("HSQL Database Engine", metaData.getDatabaseProductName());
    assertNotNull(injector.getInstance(QueryRunner.class));
    assertNotNull(injector.getInstance(AsyncQueryRunner.class));
    application.stop();
  }

  @Test
  public void configureGroup() throws Exception {
    Configuration configuration = new ConfigurationBuilder()
        .withConfigurationSource(new FileConfigurationSource("application_group.properties"))
        .build();
    ZhengApplication application = ZhengApplicationBuilder.create()
        .addModule(new DataSourceModule())
        .enableAutoLoadModule()
        .withConfiguration(configuration)
        .build();
    application.start();
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
    assertNotNull(injector.getInstance(Key.get(QueryRunner.class, named("a"))));
    assertNotNull(injector.getInstance(Key.get(AsyncQueryRunner.class, named("a"))));
    assertNotNull(injector.getInstance(Key.get(QueryRunner.class, named("b"))));
    assertNotNull(injector.getInstance(Key.get(AsyncQueryRunner.class, named("b"))));
    application.stop();
  }
}