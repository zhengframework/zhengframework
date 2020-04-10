package com.github.zhengframework.metrics.example;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.github.zhengframework.metrics.MetricsModule;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import java.util.function.BiConsumer;

public class MetricsModuleExample {

  public static void main(String[] args) throws Exception {
    final Injector injector = Guice.createInjector(
        new MetricsModule()
        , new AbstractModule() {
          @Override
          protected void configure() {
            bind(TestService.class);
          }
        }
    );

    // start services
    TestService testService = injector.getInstance(TestService.class);
    for (int i = 0; i < 10; i++) {
      testService.count();
    }
    MetricRegistry metricRegistry = injector.getInstance(MetricRegistry.class);
    metricRegistry.getCounters().forEach(new BiConsumer<String, Counter>() {
      @Override
      public void accept(String s, Counter counter) {
        System.out.println(counter.getCount());
      }
    });


  }

}
