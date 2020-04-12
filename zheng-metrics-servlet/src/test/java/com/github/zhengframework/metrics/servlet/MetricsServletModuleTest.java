package com.github.zhengframework.metrics.servlet;

import com.github.zhengframework.bootstrap.Application;
import com.github.zhengframework.metrics.MetricsModule;
import com.github.zhengframework.web.WebModule;
import com.google.inject.AbstractModule;

public class MetricsServletModuleTest {

  public static void main(String[] args) throws Exception {
    Application application = Application.create(
        new MetricsModule(),
        new MetricsServletModule()
        , new WebModule()
        , new AbstractModule() {
          @Override
          protected void configure() {
            bind(OneHealthCheck.class);
          }
        }
    );
    application.start();
    TestService testService = application.getInjector().getInstance(TestService.class);
    for (int i = 0; i < 10; i++) {
      testService.count();
    }
  }
}