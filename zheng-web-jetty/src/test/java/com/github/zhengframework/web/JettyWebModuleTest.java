package com.github.zhengframework.web;

import static org.junit.Assert.assertEquals;

import com.github.zhengframework.bootstrap.ZhengApplication;
import com.github.zhengframework.bootstrap.ZhengApplicationBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import javax.websocket.server.ServerEndpointConfig;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

@Slf4j
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
    String path = PathUtils.fixPath(webConfig.getContextPath());
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

      String path = PathUtils.fixPath(webConfig.getContextPath());
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


  @Test
  public void configureProgrammingWS() throws Exception {
    String echoPath = "/echo222";
    ZhengApplication application = ZhengApplicationBuilder.create().addModule(
        new MyModule(), new AbstractModule() {
          protected void configure() {

            BasicServerEndpointConfig serverEndpointConfig = new BasicServerEndpointConfig(
                EchoEndpoint.class, echoPath);
            requestInjection(serverEndpointConfig);
            Multibinder.newSetBinder(binder(), ServerEndpointConfig.class)
                .addBinding().toInstance(serverEndpointConfig);
          }
        })
        .enableAutoLoadModule()
        .build();
    application.getInjector().getInstance(WebServerService.class).start();
    WebConfig webConfig = application.getInjector().getInstance(WebConfig.class);
    System.out.println(webConfig);
    try {
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
                      + PathUtils
                      .fixPath(
                          webConfig.getContextPath(),
                          webConfig.getWebSocketPath(),
                          echoPath)
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
    } finally {
      application.getInjector().getInstance(WebServerService.class).stop();
    }
  }

  @Test
  public void configureAnnotationWS() throws Exception {
    ZhengApplication application = ZhengApplicationBuilder.create().addModule(
        new MyModule(), new AbstractModule() {
          protected void configure() {
            Multibinder
                .newSetBinder(binder(), new TypeLiteral<Class<? extends WebSocketEndpoint>>() {
                }).addBinding().toInstance(EchoEndpoint.class);
          }
        })
        .enableAutoLoadModule()
        .build();
    application.getInjector().getInstance(WebServerService.class).start();
    WebConfig webConfig = application.getInjector().getInstance(WebConfig.class);
    System.out.println(webConfig);
    try {
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
    } finally {
      application.getInjector().getInstance(WebServerService.class).stop();
    }
  }
}