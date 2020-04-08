package com.dadazhishi.zheng.web;

import static org.junit.Assert.assertEquals;

import com.dadazhishi.zheng.service.Application;
import java.util.Objects;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.Test;

public class WebModuleExample {

  @Test
  public void main() throws Exception {
    Application application = Application.create(new WebModule(), new MyModule());
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
    } finally {
      application.stop();
    }
  }

}
