package com.github.zhengframework.web;

import static org.junit.Assert.assertEquals;

import com.github.zhengframework.test.WithZhengApplication;
import com.github.zhengframework.test.ZhengApplicationRunner;
import com.google.inject.Inject;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.Test;
import org.junit.runner.RunWith;

@Slf4j
@RunWith(ZhengApplicationRunner.class)
public class UndertowWebModuleTest {

  @Inject() private WebConfig webConfig;

  @Test
  @WithZhengApplication(moduleClass = MyModule.class)
  public void configure() throws Exception {
    System.out.println(webConfig);
    OkHttpClient okHttpClient = new Builder().build();
    Request request;
    if (webConfig.getContextPath().endsWith("/")) {
      request =
          new Request.Builder()
              .url("http://localhost:" + webConfig.getPort() + webConfig.getContextPath() + "hello")
              .get()
              .build();
    } else {
      request =
          new Request.Builder()
              .url(
                  "http://localhost:" + webConfig.getPort() + webConfig.getContextPath() + "/hello")
              .get()
              .build();
    }

    Response response1 = okHttpClient.newCall(request).execute();
    String resp = Objects.requireNonNull(response1.body()).string();
    System.out.println(resp);
    assertEquals("Hello, World", resp);
  }
}
