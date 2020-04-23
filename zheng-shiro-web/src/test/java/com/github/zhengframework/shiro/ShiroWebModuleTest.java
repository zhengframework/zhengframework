package com.github.zhengframework.shiro;

import static org.junit.Assert.assertEquals;

import com.github.zhengframework.bootstrap.ZhengApplication;
import com.github.zhengframework.bootstrap.ZhengApplicationBuilder;
import com.github.zhengframework.web.WebConfig;
import com.google.inject.Injector;
import com.google.inject.servlet.ServletModule;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.junit.Test;

@Slf4j
public class ShiroWebModuleTest {

  //aaa

  public static void main(String[] args) throws Exception {
    ZhengApplication application = ZhengApplicationBuilder.create()
        .enableAutoLoadModule()
        .addModule(new ServletModule() {
          @Override
          protected void configureServlets() {
            bind(LoginServlet.class).in(Singleton.class);
            serve("/login").with(LoginServlet.class);
          }
        })
        .build();

    application.start();
    Injector injector = application.getInjector();
    SecurityManager securityManager = injector.getInstance(SecurityManager.class);
    SecurityUtils.setSecurityManager(securityManager);

    WebConfig webConfig = application.getInjector().getInstance(WebConfig.class);
    System.out.println(webConfig);

    OkHttpClient okHttpClient = new Builder()
        .build();

    Response response = okHttpClient.newCall(new Request.Builder()
        .url("http://localhost:" + webConfig.getPort() + "/login")
        .get().build()).execute();
    System.out.println(response.body().string());

    FormBody formBody = new FormBody.Builder()
        .add("username", "lonestarr")
        .add("password", "vespa")
        .build();
    Request request = new Request.Builder()
        .url("http://localhost:" + webConfig.getPort() + "/login")
        .post(formBody).build();
    System.out.println(request);
    Response response1 = okHttpClient.newCall(request).execute();
    String resp = Objects.requireNonNull(response1.body()).string();
    System.out.println(resp);
  }

  @Test
  public void configure() throws Exception {
    ZhengApplication application = ZhengApplicationBuilder.create()
        .enableAutoLoadModule()
        .addModule(new ServletModule() {
          @Override
          protected void configureServlets() {
            bind(LoginServlet.class).in(Singleton.class);
            serve("/login").with(LoginServlet.class);
          }
        })
        .build();

    application.start();
    Injector injector = application.getInjector();
    SecurityManager securityManager = injector.getInstance(SecurityManager.class);
    SecurityUtils.setSecurityManager(securityManager);

    WebConfig webConfig = application.getInjector().getInstance(WebConfig.class);
    System.out.println(webConfig);

    OkHttpClient okHttpClient = new Builder()
        .build();

    Response response = okHttpClient.newCall(new Request.Builder()
        .url("http://localhost:" + webConfig.getPort() + "/login")
        .get().build()).execute();
    System.out.println();
    assertEquals("Hello, World", Objects.requireNonNull(response.body()).string());

    FormBody formBody = new FormBody.Builder()
        .add("username", "lonestarr")
        .add("password", "vespa")
        .build();
    Request request = new Request.Builder()
        .url("http://localhost:" + webConfig.getPort() + "/login")
        .post(formBody).build();
    System.out.println(request);
    Response response1 = okHttpClient.newCall(request).execute();
    String resp = Objects.requireNonNull(response1.body()).string();
    System.out.println(resp);
    assertEquals("currentUser=lonestarr\n", resp);
    for (Entry<String, List<String>> entry : response1.headers().toMultimap()
        .entrySet()) {
      System.out.println(entry.getKey());
      System.out.println(entry.getValue());
    }

//    application.stop();
  }
}