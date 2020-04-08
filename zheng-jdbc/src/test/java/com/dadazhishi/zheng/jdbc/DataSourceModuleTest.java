package com.dadazhishi.zheng.jdbc;

import static com.google.inject.name.Names.named;

import com.dadazhishi.zheng.configuration.Configuration;
import com.dadazhishi.zheng.configuration.ConfigurationBuilder;
import com.dadazhishi.zheng.configuration.source.FileConfigurationSource;
import com.dadazhishi.zheng.service.Application;
import com.google.inject.Key;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.junit.Assert;
import org.junit.Test;

public class DataSourceModuleTest {

  @Test
  public void configure() throws SQLException {
    Configuration configuration = new ConfigurationBuilder()
        .withConfigurationSource(new FileConfigurationSource("application.properties"))
        .build();

    Application application = Application.create(configuration, new DataSourceModule());
    DataSource dataSource = application.getInjector().getInstance(DataSource.class);
    Connection connection = dataSource.getConnection();
    DatabaseMetaData metaData = connection.getMetaData();
    System.out.println(metaData.getDatabaseProductName());
    Assert.assertEquals("HSQL Database Engine", metaData.getDatabaseProductName());
  }

  @Test
  public void configureGroup() throws SQLException {
    Configuration configuration = new ConfigurationBuilder()
        .withConfigurationSource(new FileConfigurationSource("application_group.properties"))
        .build();

    Application application = Application.create(configuration, new DataSourceModule());
    DataSource dataSourceA = application.getInjector()
        .getInstance(Key.get(DataSource.class, named("a")));
    DatabaseMetaData metaDataA = dataSourceA.getConnection().getMetaData();
    System.out.println(metaDataA.getDatabaseProductName());
    Assert.assertEquals("HSQL Database Engine", metaDataA.getDatabaseProductName());

    DataSource dataSourceB = application.getInjector()
        .getInstance(Key.get(DataSource.class, named("b")));
    DatabaseMetaData metaDataB = dataSourceB.getConnection().getMetaData();
    Assert.assertEquals(metaDataB.getDatabaseProductName(), metaDataA.getDatabaseProductName());
    Assert.assertNotEquals(dataSourceA, dataSourceB);


  }
}