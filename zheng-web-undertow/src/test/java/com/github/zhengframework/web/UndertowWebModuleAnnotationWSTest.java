package com.github.zhengframework.web;

import static org.junit.Assert.assertEquals;

import com.github.zhengframework.test.WithZhengApplication;
import com.github.zhengframework.test.ZhengApplicationRunner;
import com.google.inject.Inject;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.runner.RunWith;

@Slf4j
@RunWith(ZhengApplicationRunner.class)
public class UndertowWebModuleAnnotationWSTest {

  @Inject()
  private WebConfig webConfig;

  @Test
  @WithZhengApplication(moduleClass = {MyModule.class, AnnotationWSModule.class})
  public void configureAnnotationWS() throws Exception {
    System.out.println(webConfig);
    OkHttpClient okHttpClient = new Builder()
        .build();

    String path = PathUtils.fixPath(webConfig.getContextPath());
    Request request = new Request.Builder()
        .url("http://localhost:" + webConfig.getPort() + path + "/hello")
        .get().build();
    System.out.println(request);
    Response response1 = okHttpClient.newCall(request).execute();
    String resp = Objects.requireNonNull(response1.body()).string();
    System.out.println(resp);
    assertEquals("Hello, World", resp);

    WebSocket webSocket = okHttpClient.newWebSocket(new Request.Builder()
            .url(
                "ws://localhost:" + webConfig.getPort()
                    + PathUtils.fixPath(
                    webConfig.getContextPath(),
                    webConfig.getWebSocketPath(),
                    "/echo")
            ).build()

        , new WebSocketListener() {
          @Override
          public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
            log.info("onMessage={}", text);
            assertEquals("hello", text);
          }

          @Override
          public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
            webSocket.send("hello");
          }
        });

    okHttpClient.dispatcher().executorService().awaitTermination(1, TimeUnit.SECONDS);
  }


}