package com.github.zhengframework.eventbus;

import static org.junit.Assert.assertEquals;

import com.github.zhengframework.bootstrap.ZhengApplication;
import com.github.zhengframework.bootstrap.ZhengApplicationBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;

public class EventBusModuleTest {

  @org.junit.Test
  public void configure() throws Exception {
    ZhengApplication application = ZhengApplicationBuilder.create()
        .enableAutoLoadModule()
        .addModule(new AbstractModule() {
          @Override
          protected void configure() {
            bind(Pub.class);
            bind(Sub.class);
          }
        })
        .build();
    application.start();
    Injector injector = application.getInjector();

    Pub pub = injector.getInstance(Pub.class);
    for (int i = 0; i < 10; i++) {
      pub.publish();
    }
    Sub sub = injector.getInstance(Sub.class);
    assertEquals(10, sub.getCount());
    application.stop();
  }
}