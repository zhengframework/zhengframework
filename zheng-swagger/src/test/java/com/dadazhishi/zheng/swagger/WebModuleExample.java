package com.dadazhishi.zheng.swagger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.dadazhishi.zheng.rest.jersey.RestModule;
import com.dadazhishi.zheng.service.ZhengApplication;
import com.dadazhishi.zheng.web.WebConfig;
import com.dadazhishi.zheng.web.WebModule;
import java.util.Objects;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.Test;

public class WebModuleExample {

  @Test
  public void main() throws Exception {
//  public static void main(String[] args) throws Exception {
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
