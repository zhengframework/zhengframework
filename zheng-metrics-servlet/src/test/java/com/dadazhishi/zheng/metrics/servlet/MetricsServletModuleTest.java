package com.dadazhishi.zheng.metrics.servlet;

import com.dadazhishi.zheng.metrics.MetricsModule;
import com.dadazhishi.zheng.service.ZhengApplication;
import com.dadazhishi.zheng.web.WebModule;
import com.google.inject.AbstractModule;

public class MetricsServletModuleTest {

  public static void main(String[] args) throws Exception {
    ZhengApplication application = ZhengApplication.create(
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