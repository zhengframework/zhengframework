package com.github.zhengframework.jdbc.migrate;

import static org.junit.Assert.assertTrue;

import com.github.zhengframework.test.WithZhengApplication;
import com.github.zhengframework.test.ZhengApplicationRunner;
import com.google.inject.Inject;
import com.google.inject.Injector;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;

@Slf4j
@RunWith(ZhengApplicationRunner.class)
public class LiquibaseMigrateModuleTest {

  @Inject private Injector injector;

  @WithZhengApplication
  @Test
  public void configure() throws Exception {
    DataSource dataSource = injector.getInstance(DataSource.class);
    Connection connection = dataSource.getConnection();
    DatabaseMetaData metaData = connection.getMetaData();
    log.info("{}", metaData.getDatabaseProductName());
    Statement statement = connection.createStatement();
    ResultSet resultSet = statement.executeQuery("SELECT * FROM public.department;");
    assertTrue(resultSet.next());
    resultSet.close();
    statement.close();
    connection.close();
  }
}
