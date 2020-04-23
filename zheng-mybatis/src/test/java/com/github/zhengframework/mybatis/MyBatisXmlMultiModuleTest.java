package com.github.zhengframework.mybatis;

import static com.google.inject.name.Names.named;
import static org.apache.ibatis.io.Resources.getResourceAsReader;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.github.zhengframework.bootstrap.ZhengApplication;
import com.github.zhengframework.bootstrap.ZhengApplicationBuilder;
import com.github.zhengframework.configuration.Configuration;
import com.github.zhengframework.configuration.ConfigurationBuilder;
import com.github.zhengframework.configuration.source.FileConfigurationSource;
import com.github.zhengframework.jdbc.DataSourceModuleProvider;
import com.google.inject.Injector;
import com.google.inject.Key;
import java.io.IOException;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.Test;

public class MyBatisXmlMultiModuleTest {

  @Test
  public void configure() throws Exception {
    Configuration configuration = new ConfigurationBuilder()
        .withConfigurationSource(new FileConfigurationSource("application_xml_group.properties"))
        .build();
    System.out.println(configuration.asMap());
    ZhengApplication application = ZhengApplicationBuilder.create()
        .enableAutoLoadModule()
        .excludeModuleProvider(MyBatisModuleProvider.class, DataSourceModuleProvider.class)
        .addModule(new MyBatisXmlMultiModule())
        .withConfiguration(configuration)
        .build();
    application.start();
    Injector injector = application.getInjector();
    runGroup(injector, "a");
    runGroup(injector, "b");
    application.stop();
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