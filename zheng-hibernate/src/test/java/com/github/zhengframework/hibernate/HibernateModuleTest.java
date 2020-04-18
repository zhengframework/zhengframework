package com.github.zhengframework.hibernate;

import static com.google.inject.name.Names.named;
import static org.junit.Assert.assertEquals;

import com.github.zhengframework.bootstrap.ZhengApplication;
import com.github.zhengframework.bootstrap.ZhengApplicationBuilder;
import com.github.zhengframework.configuration.Configuration;
import com.github.zhengframework.configuration.MapConfiguration;
import com.github.zhengframework.hibernate.a.Work;
import com.github.zhengframework.hibernate.b.Work2;
import com.github.zhengframework.hibernate.c.Work3;
import com.google.inject.Injector;
import com.google.inject.Key;
import java.util.HashMap;
import org.junit.Test;

public class HibernateModuleTest {

  @Test
  public void simple() throws Exception {

    HashMap<String, String> map = new HashMap<>();
    map.put("zheng.hibernate.driverClassName", "org.h2.Driver");
    map.put("zheng.hibernate.url", "jdbc:h2:mem:test");
    map.put("zheng.hibernate.username", "sa");
    map.put("zheng.hibernate.entityPackages", "com.github.zhengframework.hibernate.a");
    map.put("zheng.hibernate.properties.hibernate_dialect", "org.hibernate.dialect.H2Dialect");
    map.put("zheng.hibernate.properties.hibernate_hbm2ddl_auto", "create");

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

  @Test
  public void multi() throws Exception {

    HashMap<String, String> map = new HashMap<>();
    map.put("zheng.hibernate.group", "true");

    map.put("zheng.hibernate.a.driverClassName", "org.h2.Driver");
    map.put("zheng.hibernate.a.url", "jdbc:h2:mem:test");
    map.put("zheng.hibernate.a.username", "sa");
    map.put("zheng.hibernate.a.entityPackages", "com.github.zhengframework.hibernate.a");
    map.put("zheng.hibernate.a.properties.hibernate_dialect", "org.hibernate.dialect.H2Dialect");
    map.put("zheng.hibernate.a.properties.hibernate_hbm2ddl_auto", "create");
    map.put("zheng.hibernate.a.classCollection.1", "com.github.zhengframework.hibernate.a.Work");

    map.put("zheng.hibernate.b.driverClassName", "org.h2.Driver");
    map.put("zheng.hibernate.b.url", "jdbc:h2:mem:test");
    map.put("zheng.hibernate.b.username", "sa");
    map.put("zheng.hibernate.b.entityPackages", "com.github.zhengframework.hibernate.b");
    map.put("zheng.hibernate.b.properties.hibernate_dialect", "org.hibernate.dialect.H2Dialect");
    map.put("zheng.hibernate.b.properties.hibernate_hbm2ddl_auto", "create");
    map.put("zheng.hibernate.b.classCollection.1", "com.github.zhengframework.hibernate.b.Work2");

    Configuration configuration = new MapConfiguration(map);

    ZhengApplication application = ZhengApplicationBuilder.create()
        .enableAutoLoadModule()
        .excludeModuleProvider(HibernateModuleProvider.class)
        .addModule(new HibernateMultiModule())
        .withConfiguration(configuration)
        .build();
    application.start();
    Injector injector = application.getInjector();

    Work work = injector.getInstance(Key.get(Work.class, named("a")));

    work.makeAThing();
    work.makeAThing();
    work.makeAThing();

    System.out.println("There are now " + work.countThings() + " things");

    assertEquals(3, work.countThings());

    Work2 work2 = injector.getInstance(Key.get(Work2.class, named("b")));

    work2.makeAThing();
    work2.makeAThing();
    work2.makeAThing();
    work2.makeAThing();

    System.out.println("There are now " + work2.countThings() + " things");

    assertEquals(4, work2.countThings());

    application.stop();
  }


  @Test
  public void testJavaxTransactional() throws Exception {

    HashMap<String, String> map = new HashMap<>();
    map.put("zheng.hibernate.driverClassName", "org.h2.Driver");
    map.put("zheng.hibernate.url", "jdbc:h2:mem:test");
    map.put("zheng.hibernate.username", "sa");
    map.put("zheng.hibernate.entityPackages", "com.github.zhengframework.hibernate.c");
    map.put("zheng.hibernate.properties.hibernate_dialect", "org.hibernate.dialect.H2Dialect");
    map.put("zheng.hibernate.properties.hibernate_hbm2ddl_auto", "create");

    Configuration configuration = new MapConfiguration(map);

    ZhengApplication application = ZhengApplicationBuilder.create()
        .enableAutoLoadModule()
        .withConfiguration(configuration)
        .build();
    application.start();
    Injector injector = application.getInjector();

    Work3 work = injector.getInstance(Work3.class);

    work.makeAThing();
    work.makeAThing();
    work.makeAThing();

    System.out.println("There are now " + work.countThings() + " things");

    assertEquals(3, work.countThings());

    application.stop();
  }
}
