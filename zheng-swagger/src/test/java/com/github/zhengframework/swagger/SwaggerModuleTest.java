package com.github.zhengframework.swagger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.github.zhengframework.rest.RestConfig;
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
import org.junit.Test;
import org.junit.runner.RunWith;

@Slf4j
@RunWith(ZhengApplicationRunner.class)
public class SwaggerModuleTest {

  @Inject private Injector injector;
  @Inject private WebConfig webConfig;
  @Inject private RestConfig restConfig;

  @Test
  @WithZhengApplication(moduleClass = {MyModule.class})
  public void test() throws Exception {

    log.info("{}", "webConfig=" + webConfig);
    log.info("{}", "restConfig=" + restConfig);
    String path = PathUtils.fixPath(webConfig.getContextPath(), restConfig.getPath());

    OkHttpClient okHttpClient = new Builder().build();
    Request request =
        new Request.Builder()
            .url("http://localhost:" + webConfig.getPort() + path + "/test/aa/bb")
            .get()
            .build();

    Response response1 = okHttpClient.newCall(request).execute();
    String resp = Objects.requireNonNull(response1.body()).string();
    log.info("{}", resp);
    assertEquals("aa/bb", resp);

    String openapiJSON =
        okHttpClient
            .newCall(
                new Request.Builder()
                    .url("http://localhost:" + webConfig.getPort() + path + "/openapi.json")
                    .get()
                    .build())
            .execute()
            .body()
            .string();
    assertTrue(openapiJSON.contains("/test/{a}/{b}"));
    String openapiYAML =
        okHttpClient
            .newCall(
                new Request.Builder()
                    .url("http://localhost:" + webConfig.getPort() + path + "/openapi.yaml")
                    .get()
                    .build())
            .execute()
            .body()
            .string();
    assertTrue(openapiYAML.contains("/test/{a}/{b}"));
  }
}
