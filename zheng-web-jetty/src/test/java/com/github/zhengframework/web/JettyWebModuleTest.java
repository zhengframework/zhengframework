package com.github.zhengframework.web;

import static org.junit.Assert.assertEquals;

import com.github.zhengframework.bootstrap.ZhengApplication;
import com.github.zhengframework.bootstrap.ZhengApplicationBuilder;
import java.util.Objects;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.Test;

public class JettyWebModuleTest {

  public static void main(String[] aaa) throws Exception {
    ZhengApplication application = ZhengApplicationBuilder.create().addModule(
        new MyModule())
        .enableAutoLoadModule()
        .build();
    application.getInjector().getInstance(WebServerService.class).start();
    WebConfig webConfig = application.getInjector().getInstance(WebConfig.class);
    System.out.println(webConfig);
    OkHttpClient okHttpClient = new Builder()
        .build();
    String path=PathUtils.fixPath(webConfig.getContextPath());
    Request request = new Request.Builder()
        .url("http://localhost:" + webConfig.getPort() + path + "/hello")
        .get().build();
    System.out.println(request);
    Response response1 = okHttpClient.newCall(request).execute();
    String resp = Objects.requireNonNull(response1.body()).string();
    System.out.println(resp);
    assertEquals("Hello, World", resp);
  }

  @Test
  public void configure() throws Exception {
    ZhengApplication application = ZhengApplicationBuilder.create().addModule(
        new MyModule())
        .enableAutoLoadModule()
        .build();
    application.getInjector().getInstance(WebServerService.class).start();
    WebConfig webConfig = application.getInjector().getInstance(WebConfig.class);
    System.out.println(webConfig);
    try {
      OkHttpClient okHttpClient = new Builder()
          .build();

      String path=PathUtils.fixPath(webConfig.getContextPath());
      Request request = new Request.Builder()
          .url("http://localhost:" + webConfig.getPort() + path + "/hello")
          .get().build();
      System.out.println(request);
      Response response1 = okHttpClient.newCall(request).execute();
      String resp = Objects.requireNonNull(response1.body()).string();
      System.out.println(resp);
      assertEquals("Hello, World", resp);
    } finally {
      application.getInjector().getInstance(WebServerService.class).stop();
    }
  }

}