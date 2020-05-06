package com.github.zhengframework.jdbc;

import static com.google.inject.name.Names.named;
import static org.junit.Assert.assertNotNull;

import com.github.zhengframework.test.WithZhengApplication;
import com.github.zhengframework.test.ZhengApplicationRunner;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sql2o.Sql2o;

@RunWith(ZhengApplicationRunner.class)
public class Sql2oModuleTest {

  @Inject private Injector injector;

  @Test
  @WithZhengApplication(configFile = "application.properties")
  public void configure() throws SQLException {
    DataSource dataSource = injector.getInstance(DataSource.class);
    Connection connection = dataSource.getConnection();
    DatabaseMetaData metaData = connection.getMetaData();
    System.out.println(metaData.getDatabaseProductName());
    Assert.assertEquals("HSQL Database Engine", metaData.getDatabaseProductName());
    Sql2o instance = injector.getInstance(Sql2o.class);
    assertNotNull(instance);
  }

  @Test
  @WithZhengApplication(configFile = "application_group.properties")
  public void configureGroup() throws SQLException {
    DataSource dataSourceA = injector.getInstance(Key.get(DataSource.class, named("a")));
    DatabaseMetaData metaDataA = dataSourceA.getConnection().getMetaData();
    System.out.println(metaDataA.getDatabaseProductName());
    Assert.assertEquals("HSQL Database Engine", metaDataA.getDatabaseProductName());

    DataSource dataSourceB = injector.getInstance(Key.get(DataSource.class, named("b")));
    DatabaseMetaData metaDataB = dataSourceB.getConnection().getMetaData();
    Assert.assertEquals(metaDataB.getDatabaseProductName(), metaDataA.getDatabaseProductName());
    Assert.assertNotEquals(dataSourceA, dataSourceB);
    assertNotNull(injector.getInstance(Key.get(Sql2o.class, named("a"))));
    assertNotNull(injector.getInstance(Key.get(Sql2o.class, named("b"))));
  }
}
