package com.github.zhengframework.web;

import static org.junit.Assert.assertEquals;

import com.github.zhengframework.service.Application;
import java.util.Objects;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.Test;

public class JettyWebModuleTest {

  public static void main(String[] aaa) throws Exception {
    Application application = Application.create(new JettyWebModule(), new MyModule());
    application.getInjector().getInstance(WebServerService.class).start();
    WebConfig webConfig = application.getInjector().getInstance(WebConfig.class);

    OkHttpClient okHttpClient = new Builder()
        .build();
    Request request = new Request.Builder()
        .url("http://localhost:" + webConfig.getPort() + webConfig.getContextPath() + "hello")
        .get().build();
    Response response1 = okHttpClient.newCall(request).execute();
    String resp = Objects.requireNonNull(response1.body()).string();
    System.out.println(resp);
    assertEquals("Hello, World", resp);

  }

  @Test
  public void configureServlets() throws Exception {
    Application application = Application.create(new JettyWebModule(), new MyModule());
    application.getInjector().getInstance(WebServerService.class).start();
    WebConfig webConfig = application.getInjector().getInstance(WebConfig.class);

    try {
      OkHttpClient okHttpClient = new Builder()
          .build();
      Request request;
      if (webConfig.getContextPath().endsWith("/")) {
        request = new Request.Builder()
            .url("http://localhost:" + webConfig.getPort() + webConfig.getContextPath() + "hello")
            .get().build();
      } else {
        request = new Request.Builder()
            .url("http://localhost:" + webConfig.getPort() + webConfig.getContextPath() + "/hello")
            .get().build();
      }

      Response response1 = okHttpClient.newCall(request).execute();
      String resp = Objects.requireNonNull(response1.body()).string();
      System.out.println(resp);
      assertEquals("Hello, World", resp);
    } finally {
//      application.stop();
      application.getInjector().getInstance(WebServerService.class).stop();
    }
  }
}