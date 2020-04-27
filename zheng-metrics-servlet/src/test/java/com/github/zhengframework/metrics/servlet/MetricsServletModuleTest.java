package com.github.zhengframework.metrics.servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.codahale.metrics.MetricRegistry;
import com.github.zhengframework.test.WithZhengApplication;
import com.github.zhengframework.test.ZhengApplicationRunner;
import com.github.zhengframework.web.PathUtils;
import com.github.zhengframework.web.WebConfig;
import com.google.inject.Inject;
import com.google.inject.Injector;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;

@Slf4j
@RunWith(ZhengApplicationRunner.class)
public class MetricsServletModuleTest {

  @Inject
  private Injector injector;

  @WithZhengApplication(moduleClass = {MyModule.class})
  @Test
  public void test() throws Exception {
    TestService testService = injector.getInstance(TestService.class);
    for (int i = 0; i < 10; i++) {
      testService.count();
    }
    MetricRegistry metricRegistry = injector.getInstance(MetricRegistry.class);

    assertEquals(10, metricRegistry
        .counter(TestService.class.getName() + ".count.current").getCount());
    assertEquals(10,
        metricRegistry.timer(TestService.class.getName() + ".count.timer")
            .getCount());
    assertEquals(10,
        metricRegistry.meter(TestService.class.getName() + ".count.meter")
            .getCount());
    metricRegistry.getGauges().forEach((key, value) -> {
      System.out.println(key);
      System.out.println(value.getValue());
      if ((TestService.class.getName() + ".count.gauge").equals(key)) {
        assertEquals("1111", value.getValue());
      }
    });
    WebConfig webConfig = injector.getInstance(WebConfig.class);
    MetricsServletConfig metricsServletConfig = injector.getInstance(MetricsServletConfig.class);
    String url = "http://localhost:" + webConfig.getPort() + PathUtils
        .fixPath(webConfig.getContextPath(), metricsServletConfig.getPath()) + "/";
    log.info("Metrics Admin Page: {}", url);

    OkHttpClient okHttpClient = new Builder()
        .build();
    Request request = new Request.Builder()
        .url(url)
        .get().build();

    Response response1 = okHttpClient.newCall(request).execute();
    String resp = Objects.requireNonNull(response1.body()).string();
    System.out.println(resp);
    assertTrue(StringUtils.contains(resp, "<h1>Operational Menu</h1>"));
  }
}