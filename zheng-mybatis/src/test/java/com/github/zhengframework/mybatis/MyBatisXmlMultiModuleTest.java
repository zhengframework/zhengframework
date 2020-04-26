package com.github.zhengframework.mybatis;

import static com.google.inject.name.Names.named;
import static org.apache.ibatis.io.Resources.getResourceAsReader;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.github.zhengframework.jdbc.DataSourceModuleProvider;
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
public class MyBatisXmlMultiModuleTest {

  @Inject
  private Injector injector;

  @WithZhengApplication(configFile = "application_xml_group.properties"
      , moduleClass = {MyBatisXmlMultiModule.class}
      , excludeModuleProviderClass = {
      MyBatisModuleProvider.class, DataSourceModuleProvider.class
  })
  @Test
  public void configure() throws Exception {
    runGroup(injector, "a");
    runGroup(injector, "b");
  }

  public void runGroup(Injector injector, String group) throws IOException {
    ScriptRunner runner = injector.getInstance(Key.get(ScriptRunner.class, named(group)));
    runner.setAutoCommit(true);
    runner.setStopOnError(true);
    runner.runScript(getResourceAsReader("database-schema.sql"));
    runner.runScript(getResourceAsReader("database-test-data.sql"));

    FooService fooService = injector.getInstance(Key.get(FooService.class, named(group)));
    User user = fooService.doSomeBusinessStuff("u1");
    assertNotNull(user);
    assertEquals("Pocoyo", user.getName());

    UserDAO userDAO = injector.getInstance(Key.get(UserDAO.class, named(group)));
    User user1 = userDAO.getUser("u1");
    assertEquals("Pocoyo", user1.getName());

  }
}