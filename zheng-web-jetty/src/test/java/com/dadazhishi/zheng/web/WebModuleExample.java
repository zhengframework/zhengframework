package com.dadazhishi.zheng.web;

import static org.junit.Assert.assertEquals;

import com.dadazhishi.zheng.service.Run;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.servlet.ServletModule;
import java.io.IOException;
import java.util.Objects;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.Test;

public class WebModuleExample {

  @Test
  public void main() throws Exception {
    final Injector injector = Guice.createInjector(
        new TestHandlerModule(),
        new MyModule(),
        new WebModule());

    Run run = injector.getInstance(Run.class);
    WebConfig webConfig = injector.getInstance(WebConfig.class);
    run.start();
    try {

      OkHttpClient okHttpClient = new Builder()
          .build();
      Request request = new Request.Builder()
          .url("http://localhost:" + webConfig.getPort() + "/hello").get().build();
      Response response1 = okHttpClient.newCall(request).execute();
      String resp = Objects.requireNonNull(response1.body()).string();
      System.out.println(resp);
      assertEquals("Hello, World", resp);
    } finally {
      run.stop();
    }
  }

  @Singleton
  public static class HelloServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
      resp.getWriter().print("Hello, World");
    }
  }

  public static class MyModule extends ServletModule {

    @Override
    protected void configureServlets() {
      serve("/hello").with(HelloServlet.class);
    }

    @Provides
    @Singleton
    public WebConfig webConfig() {
      WebConfig cfg = new WebConfig();
      cfg.setPort(8888);  // default is 8080
      return cfg;
    }
  }

  public static class TestHandlerModule extends AbstractModule {

    @Override
    protected void configure() {
    }
  }
}
