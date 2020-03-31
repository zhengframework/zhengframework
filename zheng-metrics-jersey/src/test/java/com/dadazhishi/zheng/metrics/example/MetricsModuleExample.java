package com.dadazhishi.zheng.metrics.example;

import com.dadazhishi.zheng.metrics.MetricsModule;
import com.dadazhishi.zheng.metrics.jersey.MetricsResourceConfig;
import com.dadazhishi.zheng.rest.jersey.RestModule;
import com.dadazhishi.zheng.service.Run;
import com.dadazhishi.zheng.web.WebConfig;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class MetricsModuleExample {

  public static void main(String[] args) throws Exception {
    final Injector injector = Guice.createInjector(
        new MetricsModule()
        , new RestModule(MetricsResourceConfig.class)
        , new AbstractModule() {
          @Override
          protected void configure() {

            bind(TestResource.class);
            bind(TestService.class);
            bind(WebConfig.class).toInstance(new WebConfig());
          }
        }
    );

    // start services
    injector.getInstance(Run.class).start();
  }

}
