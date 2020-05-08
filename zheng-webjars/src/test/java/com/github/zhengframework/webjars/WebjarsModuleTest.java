package com.github.zhengframework.webjars;

import static org.junit.Assert.assertEquals;

import com.github.zhengframework.test.WithZhengApplication;
import com.github.zhengframework.test.ZhengApplicationRunner;
import com.github.zhengframework.web.WebConfig;
import com.google.inject.Inject;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import org.junit.Test;
import org.junit.runner.RunWith;

@Slf4j
@RunWith(ZhengApplicationRunner.class)
public class WebjarsModuleTest {

  @Inject private WebConfig webConfig;

  @Inject private WebjarsConfig webjarsConfig;

  @Test
  @WithZhengApplication(configFile = "app.properties")
  public void testDefaultPath() throws Exception {
    log.info("{}", webConfig);
    log.info("{}", webjarsConfig);

    OkHttpClient okHttpClient = new Builder().build();

    String bootstrap1 =
        Objects.requireNonNull(
                okHttpClient
                    .newCall(
                        new Request.Builder()
                            .url(
                                "http://localhost:"
                                    + webConfig.getPort()
                                    + webjarsConfig.getBasePath()
                                    + "/bootstrap/js/bootstrap.js")
                            .get()
                            .build())
                    .execute()
                    .body())
            .string();
    String bootstrap2 =
        Objects.requireNonNull(
                okHttpClient
                    .newCall(
                        new Request.Builder()
                            .url(
                                "http://localhost:"
                                    + webConfig.getPort()
                                    + webjarsConfig.getBasePath()
                                    + "/bootstrap/js/bootstrap.js")
                            .get()
                            .build())
                    .execute()
                    .body())
            .string();
    assertEquals(bootstrap1, bootstrap2);
    String bootstrap3 =
        Objects.requireNonNull(
                okHttpClient
                    .newCall(
                        new Request.Builder()
                            .url(
                                "http://localhost:"
                                    + webConfig.getPort()
                                    + webjarsConfig.getBasePath()
                                    + "/bootstrap/bootstrap.js")
                            .get()
                            .build())
                    .execute()
                    .body())
            .string();
    assertEquals(bootstrap1, bootstrap3);
  }

  @Test
  @WithZhengApplication(configFile = "app_cache.properties")
  public void testDisableCache() throws Exception {
    log.info("{}", webConfig);
    log.info("{}", webjarsConfig);
    OkHttpClient okHttpClient = new Builder().build();

    String bootstrap1 =
        Objects.requireNonNull(
                okHttpClient
                    .newCall(
                        new Request.Builder()
                            .url(
                                "http://localhost:"
                                    + webConfig.getPort()
                                    + webjarsConfig.getBasePath()
                                    + "/bootstrap/js/bootstrap.js")
                            .get()
                            .build())
                    .execute()
                    .body())
            .string();
    String bootstrap2 =
        Objects.requireNonNull(
                okHttpClient
                    .newCall(
                        new Request.Builder()
                            .url(
                                "http://localhost:"
                                    + webConfig.getPort()
                                    + webjarsConfig.getBasePath()
                                    + "/bootstrap/js/bootstrap.js")
                            .get()
                            .build())
                    .execute()
                    .body())
            .string();
    assertEquals(bootstrap1, bootstrap2);
    String bootstrap3 =
        Objects.requireNonNull(
                okHttpClient
                    .newCall(
                        new Request.Builder()
                            .url(
                                "http://localhost:"
                                    + webConfig.getPort()
                                    + webjarsConfig.getBasePath()
                                    + "/bootstrap/bootstrap.js")
                            .get()
                            .build())
                    .execute()
                    .body())
            .string();
    assertEquals(bootstrap1, bootstrap3);
  }

  @Test
  @WithZhengApplication(configFile = "app_path.properties")
  public void testPath() throws Exception {
    log.info("{}", webConfig);
    log.info("{}", webjarsConfig);
    OkHttpClient okHttpClient = new Builder().build();

    String bootstrap1 =
        Objects.requireNonNull(
                okHttpClient
                    .newCall(
                        new Request.Builder()
                            .url(
                                "http://localhost:"
                                    + webConfig.getPort()
                                    + webjarsConfig.getBasePath()
                                    + "/bootstrap/js/bootstrap.js")
                            .get()
                            .build())
                    .execute()
                    .body())
            .string();
    String bootstrap2 =
        Objects.requireNonNull(
                okHttpClient
                    .newCall(
                        new Request.Builder()
                            .url(
                                "http://localhost:"
                                    + webConfig.getPort()
                                    + webjarsConfig.getBasePath()
                                    + "/bootstrap/js/bootstrap.js")
                            .get()
                            .build())
                    .execute()
                    .body())
            .string();
    assertEquals(bootstrap1, bootstrap2);
    String bootstrap3 =
        Objects.requireNonNull(
                okHttpClient
                    .newCall(
                        new Request.Builder()
                            .url(
                                "http://localhost:"
                                    + webConfig.getPort()
                                    + webjarsConfig.getBasePath()
                                    + "/bootstrap/bootstrap.js")
                            .get()
                            .build())
                    .execute()
                    .body())
            .string();
    assertEquals(bootstrap1, bootstrap3);
  }
}
