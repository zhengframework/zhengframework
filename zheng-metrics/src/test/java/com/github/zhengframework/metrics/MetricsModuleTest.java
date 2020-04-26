package com.github.zhengframework.metrics;

import static org.junit.Assert.assertEquals;

import com.codahale.metrics.MetricRegistry;
import com.github.zhengframework.test.WithZhengApplication;
import com.github.zhengframework.test.ZhengApplicationRunner;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(ZhengApplicationRunner.class)
public class MetricsModuleTest {

  @Inject
  private Injector injector;

  @WithZhengApplication(moduleClass = MyModule.class)
  @Test
  public void test() throws Exception {
    TestService testService = injector.getInstance(TestService.class);
    for (int i = 0; i < 10; i++) {
      testService.count();
    }
    MetricRegistry metricRegistry = injector.getInstance(MetricRegistry.class);

    assertEquals(10, metricRegistry
        .counter("com.github.zhengframework.metrics.TestService.count.current").getCount());
    assertEquals(10,
        metricRegistry.timer("com.github.zhengframework.metrics.TestService.count.timer")
            .getCount());
    assertEquals(10,
        metricRegistry.meter("com.github.zhengframework.metrics.TestService.count.meter")
            .getCount());
    metricRegistry.getGauges().forEach((key, value) -> {
      System.out.println(key);
      System.out.println(value.getValue());
      if ("com.github.zhengframework.metrics.TestService.count.gauge".equals(key)) {
        assertEquals("1111", value.getValue());
      }
    });
  }

}
