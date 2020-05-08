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
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Jdbi;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@Slf4j
@RunWith(ZhengApplicationRunner.class)
public class JdbiModuleTest {

  @Inject private Injector injector;

  @Test
  @WithZhengApplication(configFile = "application.properties")
  public void configure() throws SQLException {
    DataSource dataSource = injector.getInstance(DataSource.class);
    Connection connection = dataSource.getConnection();
    DatabaseMetaData metaData = connection.getMetaData();
    log.info("{}", metaData.getDatabaseProductName());
    Assert.assertEquals("HSQL Database Engine", metaData.getDatabaseProductName());
    Jdbi instance = injector.getInstance(Jdbi.class);
    assertNotNull(instance);
  }

  @Test
  @WithZhengApplication(configFile = "application_group.properties")
  public void configureGroup() throws SQLException {
    DataSource dataSourceA = injector.getInstance(Key.get(DataSource.class, named("a")));
    DatabaseMetaData metaDataA = dataSourceA.getConnection().getMetaData();
    log.info("{}", metaDataA.getDatabaseProductName());
    Assert.assertEquals("HSQL Database Engine", metaDataA.getDatabaseProductName());

    DataSource dataSourceB = injector.getInstance(Key.get(DataSource.class, named("b")));
    DatabaseMetaData metaDataB = dataSourceB.getConnection().getMetaData();
    Assert.assertEquals(metaDataB.getDatabaseProductName(), metaDataA.getDatabaseProductName());
    Assert.assertNotEquals(dataSourceA, dataSourceB);
    assertNotNull(injector.getInstance(Key.get(Jdbi.class, named("a"))));
    assertNotNull(injector.getInstance(Key.get(Jdbi.class, named("b"))));
  }
}
