package com.github.zhengframework.metrics.example;

import static org.junit.Assert.assertEquals;

import com.codahale.metrics.MetricRegistry;
import com.github.zhengframework.bootstrap.ZhengApplication;
import com.github.zhengframework.bootstrap.ZhengApplicationBuilder;
import com.github.zhengframework.configuration.Configuration;
import com.github.zhengframework.configuration.MapConfiguration;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

public class MetricsModuleExample {

  @Test
  public void test() throws Exception {
    Map<String, String> map = new HashMap<>();
    map.put("zheng.metrics.enable", "true");
    Configuration configuration = new MapConfiguration(map);

    ZhengApplication application = ZhengApplicationBuilder.create()
        .enableAutoLoadModule()
        .addModule(new AbstractModule() {
          @Override
          protected void configure() {
            bind(TestService.class);
          }
        })
        .withConfiguration(configuration)
        .build();

    Injector injector = application.getInjector();
    // start services
    TestService testService = injector.getInstance(TestService.class);
    for (int i = 0; i < 10; i++) {
      testService.count();
    }
    MetricRegistry metricRegistry = injector.getInstance(MetricRegistry.class);

    assertEquals(10, metricRegistry
        .counter("com.github.zhengframework.metrics.example.TestService.count.current").getCount());
    assertEquals(10,
        metricRegistry.timer("com.github.zhengframework.metrics.example.TestService.count.timer")
            .getCount());
    assertEquals(10,
        metricRegistry.meter("com.github.zhengframework.metrics.example.TestService.count.meter")
            .getCount());
    metricRegistry.getGauges().forEach((key, value) -> {
      System.out.println(key);
      System.out.println(value.getValue());
      if ("com.github.zhengframework.metrics.example.TestService.count.gauge".equals(key)) {
        assertEquals("1111", value.getValue());
      }
    });
  }

}
