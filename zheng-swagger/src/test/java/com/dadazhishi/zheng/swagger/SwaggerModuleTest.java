package com.dadazhishi.zheng.swagger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.dadazhishi.zheng.rest.RestModule;
import com.dadazhishi.zheng.service.ZhengApplication;
import com.dadazhishi.zheng.web.WebConfig;
import com.dadazhishi.zheng.web.WebModule;
import com.dadazhishi.zheng.webjars.WebjarsModule;
import java.io.IOException;
import java.util.Objects;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.Test;

public class SwaggerModuleTest {

  public static void main(String[] args) throws IOException {
    ZhengApplication application = ZhengApplication
        .create(
            new MyModule(),
            new WebModule(),
            new SwaggerModule(),
            new WebjarsModule(),
            new RestModule()
        );
    application.start();
    WebConfig webConfig = application.getInjector().getInstance(WebConfig.class);
    System.out.println(webConfig);
    String contextPath = webConfig.getContextPath();
    if (contextPath.equals("/")) {
      contextPath = "";
    }
    if (contextPath.endsWith("/")) {
      contextPath = contextPath.substring(0, contextPath.length() - 1);
    }
    String json = "http://localhost:" + webConfig.getPort() + contextPath + "/openapi.json";
    String yaml = "http://localhost:" + webConfig.getPort() + contextPath + "/openapi.yaml";
    System.out.println(json);
    System.out.println(yaml);
  }

  @Test
  public void test() throws IOException {
    ZhengApplication application = ZhengApplication
        .create(
            new MyModule(),
            new WebModule(),
            new SwaggerModule(),
            new RestModule()
        );
    application.start();
    WebConfig webConfig = application.getInjector().getInstance(WebConfig.class);
    try {
      OkHttpClient okHttpClient = new Builder()
          .build();
      Request request = new Request.Builder()
          .url("http://localhost:" + webConfig.getPort() + webConfig.getContextPath() + "/hello")
          .get().build();
      Response response1 = okHttpClient.newCall(request).execute();
      String resp = Objects.requireNonNull(response1.body()).string();
      System.out.println(resp);
      assertEquals("Hello, World", resp);

      String openapiJSON = okHttpClient.newCall(new Request.Builder()
          .url("http://localhost:" + webConfig.getPort() + webConfig.getContextPath()
              + "/openapi.json")
          .get().build()).execute().body().string();
      assertTrue(openapiJSON.contains("/test/{a}/{b}"));
      String openapiYAML = okHttpClient.newCall(new Request.Builder()
          .url("http://localhost:" + webConfig.getPort() + webConfig.getContextPath()
              + "/openapi.yaml")
          .get().build()).execute().body().string();
      assertTrue(openapiYAML.contains("/test/{a}/{b}"));
    } finally {
      application.stop();
    }
  }
}