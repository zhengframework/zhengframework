package com.github.zhengframework.jpa;


import com.github.zhengframework.bootstrap.ZhengApplication;
import com.github.zhengframework.bootstrap.ZhengApplicationBuilder;
import com.github.zhengframework.configuration.Configuration;
import com.github.zhengframework.configuration.MapConfiguration;
import com.github.zhengframework.jpa.a.Work;
import com.google.inject.Injector;
import java.util.HashMap;

public class JpaModuleTest {

  public static void test(HashMap<String, String> map) throws Exception {

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

    assert 3 == work.countThings();
    application.stop();
  }
}