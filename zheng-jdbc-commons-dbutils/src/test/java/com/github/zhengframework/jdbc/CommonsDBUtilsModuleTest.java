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
import javax.sql.DataSource;
import org.apache.commons.dbutils.AsyncQueryRunner;
import org.apache.commons.dbutils.QueryRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(ZhengApplicationRunner.class)
public class CommonsDBUtilsModuleTest {

  @Inject private Injector injector;

  @Test
  @WithZhengApplication(configFile = "application.properties")
  public void configure() throws Exception {
    DataSource dataSource = injector.getInstance(DataSource.class);
    Connection connection = dataSource.getConnection();
    DatabaseMetaData metaData = connection.getMetaData();
    System.out.println(metaData.getDatabaseProductName());
    Assert.assertEquals("HSQL Database Engine", metaData.getDatabaseProductName());
    assertNotNull(injector.getInstance(QueryRunner.class));
    assertNotNull(injector.getInstance(AsyncQueryRunner.class));
  }

  @Test
  @WithZhengApplication(configFile = "application_group.properties")
  public void configureGroup() throws Exception {
    DataSource dataSourceA = injector.getInstance(Key.get(DataSource.class, named("a")));
    DatabaseMetaData metaDataA = dataSourceA.getConnection().getMetaData();
    System.out.println(metaDataA.getDatabaseProductName());
    Assert.assertEquals("HSQL Database Engine", metaDataA.getDatabaseProductName());

    DataSource dataSourceB = injector.getInstance(Key.get(DataSource.class, named("b")));
    DatabaseMetaData metaDataB = dataSourceB.getConnection().getMetaData();
    Assert.assertEquals(metaDataB.getDatabaseProductName(), metaDataA.getDatabaseProductName());
    Assert.assertNotEquals(dataSourceA, dataSourceB);
    assertNotNull(injector.getInstance(Key.get(QueryRunner.class, named("a"))));
    assertNotNull(injector.getInstance(Key.get(AsyncQueryRunner.class, named("a"))));
    assertNotNull(injector.getInstance(Key.get(QueryRunner.class, named("b"))));
    assertNotNull(injector.getInstance(Key.get(AsyncQueryRunner.class, named("b"))));
  }
}
