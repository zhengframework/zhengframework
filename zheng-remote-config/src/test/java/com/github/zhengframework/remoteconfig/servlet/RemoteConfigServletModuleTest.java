package com.github.zhengframework.remoteconfig.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.zhengframework.remoteconfig.MyModule;
import com.github.zhengframework.remoteconfig.RemoteConfigServer;
import com.github.zhengframework.test.WithZhengApplication;
import com.github.zhengframework.test.ZhengApplicationRunner;
import com.github.zhengframework.web.WebConfig;
import java.io.IOException;
import java.util.Objects;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@Slf4j
@RunWith(ZhengApplicationRunner.class)
public class RemoteConfigServletModuleTest {

  @Inject private RemoteConfigServer remoteConfigServer;

  @Inject private WebConfig webConfig;

  @Inject private RemoteConfigServerServletConfig remoteConfigServerServletConfig;

  @Inject private ObjectMapper objectMapper;

  @Test
  @WithZhengApplication(moduleClass = {MyModule.class})
  public void configureServlets() throws IOException {
    OkHttpClient okHttpClient = new Builder().build();
    Response response =
        okHttpClient
            .newCall(
                new Request.Builder()
                    .url(
                        "http://localhost:"
                            + webConfig.getPort()
                            + remoteConfigServerServletConfig.getBasePath()
                            + "/?configNames=echo&configNames=testNotFound")
                    .get()
                    .addHeader("k1", "v1")
                    .build())
            .execute();

    String string = Objects.requireNonNull(response.body()).string();
    log.info("string={}", string);
    Assert.assertEquals(
        "{\"testNotFound\":{\"data\":null,\"success\":false,\"message\":\"'testNotFound' not found\"},\"echo\":{\"data\":[{\"key\":\"k1\",\"value\":\"v1\"},{\"key\":\"Connection\",\"value\":\"keep-alive\"},{\"key\":\"User-Agent\",\"value\":\"okhttp/4.6.0\"},{\"key\":\"Host\",\"value\":\"localhost:8080\"},{\"key\":\"Accept-Encoding\",\"value\":\"gzip\"}],\"success\":true,\"message\":null}}",
        string);
  }
}
