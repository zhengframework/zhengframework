package com.github.zhengframework.webjars;

import static org.junit.Assert.assertEquals;

import com.github.zhengframework.configuration.Configuration;
import com.github.zhengframework.configuration.ConfigurationBuilder;
import com.github.zhengframework.configuration.source.FileConfigurationSource;
import com.github.zhengframework.service.Application;
import com.github.zhengframework.web.WebConfig;
import com.github.zhengframework.web.WebModule;
import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import org.junit.Test;

public class WebjarsModuleTest {

  @Test
  public void testDefaultPath() throws IOException {
    Configuration configuration = new ConfigurationBuilder()
        .withConfigurationSource(new FileConfigurationSource("app.properties"))
        .build();
    Application application = Application
        .create(configuration,
            new WebjarsModule()
        );
    application.start();
    WebConfig webConfig = application.getInjector().getInstance(WebConfig.class);
    WebjarsConfig webjarsConfig = application.getInjector().getInstance(WebjarsConfig.class);
    System.out.println(webConfig);
    System.out.println(webjarsConfig);
    try {
      OkHttpClient okHttpClient = new Builder()
          .build();

      String bootstrap1 = okHttpClient.newCall(new Request.Builder()
          .url("http://localhost:" + webConfig.getPort()
              + webjarsConfig.getBasePath() + "/bootstrap/3.1.0/js/bootstrap.js")
          .get().build()).execute().body().string();
      String bootstrap2 = okHttpClient.newCall(new Request.Builder()
          .url("http://localhost:" + webConfig.getPort()
              + webjarsConfig.getBasePath() + "/bootstrap/js/bootstrap.js")
          .get().build()).execute().body().string();
      assertEquals(bootstrap1, bootstrap2);
      String bootstrap3 = okHttpClient.newCall(new Request.Builder()
          .url("http://localhost:" + webConfig.getPort()
              + webjarsConfig.getBasePath() + "/bootstrap/bootstrap.js")
          .get().build()).execute().body().string();
      assertEquals(bootstrap1, bootstrap3);
    } finally {
      application.stop();
    }
  }

  @Test
  public void testDisableCache() throws IOException {
    Configuration configuration = new ConfigurationBuilder()
        .withConfigurationSource(new FileConfigurationSource("app_cache.properties"))
        .build();
    Application application = Application
        .create(configuration,
            new WebjarsModule()
        );
    application.start();
    WebConfig webConfig = application.getInjector().getInstance(WebConfig.class);
    WebjarsConfig webjarsConfig = application.getInjector().getInstance(WebjarsConfig.class);
    System.out.println(webConfig);
    System.out.println(webjarsConfig);
    try {
      OkHttpClient okHttpClient = new Builder()
          .build();

      String bootstrap1 = okHttpClient.newCall(new Request.Builder()
          .url("http://localhost:" + webConfig.getPort()
              + webjarsConfig.getBasePath() + "/bootstrap/3.1.0/js/bootstrap.js")
          .get().build()).execute().body().string();
      String bootstrap2 = okHttpClient.newCall(new Request.Builder()
          .url("http://localhost:" + webConfig.getPort()
              + webjarsConfig.getBasePath() + "/bootstrap/js/bootstrap.js")
          .get().build()).execute().body().string();
      assertEquals(bootstrap1, bootstrap2);
      String bootstrap3 = okHttpClient.newCall(new Request.Builder()
          .url("http://localhost:" + webConfig.getPort()
              + webjarsConfig.getBasePath() + "/bootstrap/bootstrap.js")
          .get().build()).execute().body().string();
      assertEquals(bootstrap1, bootstrap3);
    } finally {
      application.stop();
    }
  }

  @Test
  public void testPath() throws IOException {
    Configuration configuration = new ConfigurationBuilder()
        .withConfigurationSource(new FileConfigurationSource("app_path.properties"))
        .build();
    Application application = Application
        .create(configuration,
            new WebModule(),
            new WebjarsModule()
        );
    application.start();
    WebConfig webConfig = application.getInjector().getInstance(WebConfig.class);
    WebjarsConfig webjarsConfig = application.getInjector().getInstance(WebjarsConfig.class);
    System.out.println(webConfig);
    System.out.println(webjarsConfig);
    try {
      OkHttpClient okHttpClient = new Builder()
          .build();

      String bootstrap1 = okHttpClient.newCall(new Request.Builder()
          .url("http://localhost:" + webConfig.getPort()
              + webjarsConfig.getBasePath() + "/bootstrap/3.1.0/js/bootstrap.js")
          .get().build()).execute().body().string();
      String bootstrap2 = okHttpClient.newCall(new Request.Builder()
          .url("http://localhost:" + webConfig.getPort()
              + webjarsConfig.getBasePath() + "/bootstrap/js/bootstrap.js")
          .get().build()).execute().body().string();
      assertEquals(bootstrap1, bootstrap2);
      String bootstrap3 = okHttpClient.newCall(new Request.Builder()
          .url("http://localhost:" + webConfig.getPort()
              + webjarsConfig.getBasePath() + "/bootstrap/bootstrap.js")
          .get().build()).execute().body().string();
      assertEquals(bootstrap1, bootstrap3);
    } finally {
      application.stop();
    }
  }
}