package com.github.zhengframework.rest;

import com.github.zhengframework.bootstrap.ZhengApplication;
import com.github.zhengframework.bootstrap.ZhengApplicationBuilder;
import com.github.zhengframework.configuration.Configuration;
import com.github.zhengframework.configuration.ConfigurationBuilder;
import com.github.zhengframework.configuration.source.FileConfigurationSource;
import com.github.zhengframework.web.WebConfig;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RestExample {

  WebConfig webConfig;
  ZhengApplication application;

  @After
  public void stop() {
    application.stop();
  }

  @Before
  public void setup() throws Exception {
    Configuration configuration = new ConfigurationBuilder()
        .withConfigurationSource(new FileConfigurationSource("app.properties"))
        .build();
    System.out.println(configuration.asMap());
    application = ZhengApplicationBuilder.create()
        .addModule(
            new MyModule(),
            new RestModule()
        )
        .enableAutoLoadModule()
        .withConfiguration(configuration)
        .build();
    webConfig = application.getInjector().getInstance(WebConfig.class);
    application.start();
  }

  @Test
  public void start() throws Exception {

    Client client = ClientBuilder.newClient();
    WebTarget target = client.target("http://localhost:" + webConfig.getPort())
        .path("/");
    String response = target.path(TestResource.PATH).request().get().readEntity(String.class);
    System.out.println(response);
    Assert.assertEquals(TestResource.MESSAGE, response);
  }

  @Test
  public void testInject() throws Exception {
    Client client = ClientBuilder.newClient();
    WebTarget target = client.target("http://localhost:" + webConfig.getPort())
        .path(webConfig.getContextPath());
    String response1 = target.path(TestResource.PATH + "/inject").request().get()
        .readEntity(String.class);
    System.out.println(response1);
    String response2 = target.path(TestResource.PATH + "/inject").request().get()
        .readEntity(String.class);
    System.out.println(response2);
    Assert.assertNotEquals(response1, response2);
  }

}