package com.github.zhengframework.swagger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.github.zhengframework.bootstrap.ZhengApplication;
import com.github.zhengframework.bootstrap.ZhengApplicationBuilder;
import com.github.zhengframework.rest.RestConfig;
import com.github.zhengframework.web.PathUtils;
import com.github.zhengframework.web.WebConfig;
import java.util.Objects;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.Test;

public class SwaggerModuleTest {

  public static void main(String[] args) throws Exception {
    ZhengApplication application = ZhengApplicationBuilder.create()
        .addModule(new MyModule())
        .enableAutoLoadModule()
        .build();
    application.start();
    WebConfig webConfig = application.getInjector().getInstance(WebConfig.class);
    RestConfig restConfig = application.getInjector().getInstance(RestConfig.class);
    SwaggerConfig swaggerConfig = application.getInjector().getInstance(SwaggerConfig.class);
    System.out.println(webConfig);
    System.out.println(restConfig);
    System.out.println(swaggerConfig);
    String contextPath = PathUtils
        .fixPath(webConfig.getContextPath(), restConfig.getPath());

    String json = "http://localhost:" + webConfig.getPort() + contextPath + "/openapi.json";
    String yaml = "http://localhost:" + webConfig.getPort() + contextPath + "/openapi.yaml";
    System.out.println(json);
    System.out.println(yaml);
  }

  @Test
  public void test() throws Exception {
    ZhengApplication application = ZhengApplicationBuilder.create()
        .addModule(
            new MyModule(),
            new SwaggerModule()
        )
        .enableAutoLoadModule()
        .build();
    application.start();
    WebConfig webConfig = application.getInjector().getInstance(WebConfig.class);
    System.out.println("webConfig=" + webConfig);
    RestConfig restConfig = application.getInjector().getInstance(RestConfig.class);
    System.out.println(restConfig);
    String path = PathUtils.fixPath(webConfig.getContextPath(), restConfig.getPath());

    try {
      OkHttpClient okHttpClient = new Builder()
          .build();
      Request request;
      request = new Request.Builder()
          .url("http://localhost:" + webConfig.getPort() + path + "/test/aa/bb")
          .get().build();

      Response response1 = okHttpClient.newCall(request).execute();
      String resp = Objects.requireNonNull(response1.body()).string();
      System.out.println(resp);
      assertEquals("aa/bb", resp);

      String openapiJSON = okHttpClient.newCall(new Request.Builder()
          .url("http://localhost:" + webConfig.getPort() + path
              + "/openapi.json")
          .get().build()).execute().body().string();
      assertTrue(openapiJSON.contains("/test/{a}/{b}"));
      String openapiYAML = okHttpClient.newCall(new Request.Builder()
          .url("http://localhost:" + webConfig.getPort() + path
              + "/openapi.yaml")
          .get().build()).execute().body().string();
      assertTrue(openapiYAML.contains("/test/{a}/{b}"));
    } finally {
      application.stop();
    }
  }
}