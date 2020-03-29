package com.dadazhishi.zheng.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import com.dadazhishi.zheng.rest.jersey.RestModule;
import com.dadazhishi.zheng.service.Run;
import com.dadazhishi.zheng.web.WebConfig;
import com.google.inject.Guice;
import com.google.inject.Injector;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import org.junit.Before;
import org.junit.Test;

public class RestExample {

  int port = 8080;
  Run run;
  WebConfig webConfig;

  @Before
  public void setup() {
    Injector injector = Guice.createInjector(new MyModule(), new RestModule());

    webConfig = injector.getInstance(WebConfig.class);
    run = injector
        .getInstance(Run.class);
  }

  @Test
  public void start() throws Exception {
    try {
      run.start();
      Client client = ClientBuilder.newClient();
      WebTarget target = client.target("http://localhost:" + port)
          .path("/");
      String response = target.path(TestResource.PATH).request().get().readEntity(String.class);
      System.out.println(response);
      assertEquals(TestResource.MESSAGE, response);
    } finally {
      run.stop();
    }
  }

  @Test
  public void testInject() throws Exception {
    try {
      run.start();
      Client client = ClientBuilder.newClient();
      WebTarget target = client.target("http://localhost:" + port)
          .path(webConfig.getContextPath());
      String response1 = target.path(TestResource.PATH + "/inject").request().get()
          .readEntity(String.class);
      System.out.println(response1);
      String response2 = target.path(TestResource.PATH + "/inject").request().get()
          .readEntity(String.class);
      System.out.println(response2);
      assertNotEquals(response1, response2);
    } finally {
      run.stop();
    }
  }

}