package com.github.zhengframework.mybatis;

import static org.apache.ibatis.io.Resources.getResourceAsReader;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.github.zhengframework.test.WithZhengApplication;
import com.github.zhengframework.test.ZhengApplicationRunner;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(ZhengApplicationRunner.class)
public class MyBatisModuleTest {

  @Inject
  private Injector injector;

  @WithZhengApplication
  @Test
  public void configure() throws Exception {
    ScriptRunner runner = injector.getInstance(ScriptRunner.class);
    runner.setAutoCommit(true);
    runner.setStopOnError(true);
    runner.runScript(getResourceAsReader("database-schema.sql"));
    runner.runScript(getResourceAsReader("database-test-data.sql"));

    FooService fooService = injector.getInstance(FooService.class);
    User user = fooService.doSomeBusinessStuff("u1");
    assertNotNull(user);
    assertEquals("Pocoyo", user.getName());

    UserDAO userDAO = injector.getInstance(UserDAO.class);
    User user1 = userDAO.getUser("u1");
    assertEquals("Pocoyo", user1.getName());
  }
}
