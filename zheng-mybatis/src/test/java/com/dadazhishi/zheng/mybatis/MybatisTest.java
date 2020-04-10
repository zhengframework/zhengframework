package com.github.zhengframework.mybatis;

import static org.apache.ibatis.io.Resources.getResourceAsReader;
import static org.junit.Assert.assertNotNull;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.PrivateModule;
import com.google.inject.name.Names;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mybatis.guice.MyBatisModule;
import org.mybatis.guice.datasource.builtin.PooledDataSourceProvider;
import org.mybatis.guice.datasource.helper.JdbcHelper;

public class MybatisTest {

  private Injector injector;

  protected static Properties createTestProperties() {
    Properties myBatisProperties = new Properties();
    myBatisProperties.setProperty("mybatis.environment.id", "test");
    myBatisProperties.setProperty("JDBC.username", "sa");
    myBatisProperties.setProperty("JDBC.password", "");
    myBatisProperties.setProperty("JDBC.autoCommit", "false");
    return myBatisProperties;
  }

  @Before
  public void setUp() throws Exception {

    injector = Guice.createInjector(
        new PrivateModule() {
          @Override
          protected void configure() {

          }
        },
        new MyBatisModule() {

          @Override
          protected void initialize() {
            install(JdbcHelper.HSQLDB_IN_MEMORY_NAMED);
            bindDataSourceProviderType(PooledDataSourceProvider.class);
            bindTransactionFactoryType(JdbcTransactionFactory.class);
            addMapperClass(UserMapper.class);
            addMapperClass(UserMapper2.class);
            Names.bindProperties(binder(), createTestProperties());
            bind(UserDAO.class).to(UserDAOImpl.class);
            bind(FooService.class).to(FooServiceMapperImpl.class);
          }

        }
    );
    Environment environment = this.injector.getInstance(SqlSessionFactory.class).getConfiguration()
        .getEnvironment();
    DataSource dataSource = environment.getDataSource();
    ScriptRunner runner = new ScriptRunner(dataSource.getConnection());
    runner.setAutoCommit(true);
    runner.setStopOnError(true);
    runner.runScript(getResourceAsReader("database-schema.sql"));
    runner.runScript(getResourceAsReader("database-test-data.sql"));

  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void test() {
    FooService fooService = injector.getInstance(FooService.class);
    User user = fooService.doSomeBusinessStuff("u1");
    assertNotNull(user);
    assertEquals("Pocoyo", user.getName());

    UserDAO userDAO = injector.getInstance(UserDAO.class);
    User user1 = userDAO.getUser("u1");
    assertEquals("Pocoyo", user1.getName());

    User user2 = fooService.doSomeBusinessStuff2("u1");
    assertEquals("Pocoyo", user2.getName());
  }
}