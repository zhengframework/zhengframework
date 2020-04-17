package com.github.zhengframework.hibernate;

import static org.junit.Assert.assertEquals;

import com.github.zhengframework.bootstrap.ZhengApplication;
import com.github.zhengframework.bootstrap.ZhengApplicationBuilder;
import com.github.zhengframework.configuration.Configuration;
import com.github.zhengframework.configuration.MapConfiguration;
import com.google.inject.Injector;
import java.util.HashMap;
import org.junit.Test;

public class HibernatePersistModuleTest {


  @Test
  public void example() throws Exception {

    HashMap<String, String> map = new HashMap<>();
    map.put("zheng.hibernate.driverClassName", "org.h2.Driver");
    map.put("zheng.hibernate.url", "jdbc:h2:mem:test");
    map.put("zheng.hibernate.username", "sa");
    map.put("zheng.hibernate.entityPackages",
        HibernatePersistModuleTest.class.getPackage().getName());
//    map.put("zheng.hibernate.properties.hibernate_dialect", "org.hibernate.dialect.H2Dialect");
    map.put("zheng.hibernate.properties.hibernate.dialect", "org.hibernate.dialect.H2Dialect");
//    map.put("zheng.hibernate.properties.hibernate_hbm2ddl_auto", "create");
    map.put("zheng.hibernate.properties.hibernate.hbm2ddl.auto", "create");

    Configuration configuration = new MapConfiguration(map);

    ZhengApplication application = ZhengApplicationBuilder.create()
        .enableAutoLoadModule()
        .withConfiguration(configuration)
        .build();
    application.start();
    Injector injector = application.getInjector();

    Work work = injector.getInstance(Work.class);

    work.makeAThing();
    work.makeAThing();
    work.makeAThing();

    System.out.println("There are now " + work.countThings() + " things");

    assertEquals(3, work.countThings());

    application.stop();
  }

}
