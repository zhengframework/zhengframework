package com.github.zhengframework.metrics.servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.codahale.metrics.MetricRegistry;
import com.github.zhengframework.bootstrap.ZhengApplication;
import com.github.zhengframework.bootstrap.ZhengApplicationBuilder;
import com.github.zhengframework.configuration.Configuration;
import com.github.zhengframework.configuration.MapConfiguration;
import com.github.zhengframework.web.PathUtils;
import com.github.zhengframework.web.WebConfig;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

@Slf4j
public class MetricsServletModuleTest {

  static ZhengApplication application;
  static Injector injector;

  public static void main(String[] args) throws Exception {
    before();
    TestService testService = application.getInjector().getInstance(TestService.class);
    for (int i = 0; i < 10; i++) {
      testService.count();
    }

    WebConfig webConfig = injector.getInstance(WebConfig.class);
    MetricsServletConfig metricsServletConfig = injector.getInstance(MetricsServletConfig.class);
    log.info("Metrics Admin Page: {}", "http://localhost:" + webConfig.getPort() + PathUtils
        .fixPath(webConfig.getContextPath(), metricsServletConfig.getPath()) + "/");
  }

  private static void before() throws Exception {
    Map<String, String> map = new HashMap<>();
    map.put("zheng.web.contextPath", "/qwerty");
    map.put("zheng.metrics.enable", "true");
    map.put("zheng.metrics.servlet.enable", "true");
    Configuration configuration = new MapConfiguration(map);

    application = ZhengApplicationBuilder.create().addModule(
        new AbstractModule() {
          @Override
          protected void configure() {
            bind(OneHealthCheck.class);
            bind(TestService.class);
          }
        })
        .enableAutoLoadModule()
        .withConfiguration(configuration)
        .build();

    injector = application.getInjector();
    application.start();
  }

  @Test
  public void test() throws Exception {
    before();
    TestService testService = application.getInjector().getInstance(TestService.class);
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
    application.stop();
  }
}