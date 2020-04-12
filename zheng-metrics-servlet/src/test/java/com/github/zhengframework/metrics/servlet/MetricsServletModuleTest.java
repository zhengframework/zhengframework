package com.github.zhengframework.metrics.servlet;

import com.github.zhengframework.bootstrap.ZhengApplication;
import com.github.zhengframework.bootstrap.ZhengApplicationBuilder;
import com.github.zhengframework.metrics.MetricsModule;
import com.github.zhengframework.web.WebModule;
import com.google.inject.AbstractModule;

public class MetricsServletModuleTest {

  public static void main(String[] args) throws Exception {

    ZhengApplication application = ZhengApplicationBuilder.create().addModule(new MetricsModule(),
        new MetricsServletModule()
        , new WebModule()
        , new AbstractModule() {
          @Override
          protected void configure() {
            bind(OneHealthCheck.class);
          }
        })
        .enableAutoLoadModule()
        .build();
    application.start();
    TestService testService = application.getInjector().getInstance(TestService.class);
    for (int i = 0; i < 10; i++) {
      testService.count();
    }
  }
}