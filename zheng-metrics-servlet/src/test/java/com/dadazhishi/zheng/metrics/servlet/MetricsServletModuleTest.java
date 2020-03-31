package com.dadazhishi.zheng.metrics.servlet;

import com.dadazhishi.zheng.service.Run;
import com.dadazhishi.zheng.web.WebConfig;
import com.dadazhishi.zheng.web.WebModule;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class MetricsServletModuleTest {

  public static void main(String[] args) throws Exception {
    final Injector injector = Guice.createInjector(
        new MetricsServletModule()
        , new WebModule()
        , new AbstractModule() {
          @Override
          protected void configure() {
            bind(WebConfig.class).toInstance(new WebConfig());
          }
        }
    );

    // start services
    injector.getInstance(Run.class).start();
  }
}