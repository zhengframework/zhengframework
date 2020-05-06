package com.github.zhengframework.mybatis;

import static com.google.inject.name.Names.named;
import static org.apache.ibatis.io.Resources.getResourceAsReader;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.github.zhengframework.test.WithZhengApplication;
import com.github.zhengframework.test.ZhengApplicationRunner;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import java.io.IOException;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(ZhengApplicationRunner.class)
public class MyBatisMultiModuleTest {

  @Inject private Injector injector;

  @WithZhengApplication(
      configFile = "application_group.properties",
      moduleClass = {MyBatisMultiModule.class},
      excludeModuleProviderClass = {MyBatisModuleProvider.class})
  @Test
  public void configure() throws Exception {
    runGroup(injector, "a");
    runGroup(injector, "b");
  }

  private void runGroup(Injector injector, String group) throws IOException {
    ScriptRunner scriptRunner = injector.getInstance(Key.get(ScriptRunner.class, named(group)));
    scriptRunner.setAutoCommit(true);
    scriptRunner.setStopOnError(true);
    scriptRunner.runScript(getResourceAsReader("database-schema.sql"));
    scriptRunner.runScript(getResourceAsReader("database-test-data.sql"));

    FooService fooService = injector.getInstance(Key.get(FooService.class, named(group)));
    User user = fooService.doSomeBusinessStuff("u1");
    assertNotNull(user);
    assertEquals("Pocoyo", user.getName());

    UserDAO userDAO = injector.getInstance(Key.get(UserDAO.class, named(group)));
    User user1 = userDAO.getUser("u1");
    assertEquals("Pocoyo", user1.getName());
  }
}
