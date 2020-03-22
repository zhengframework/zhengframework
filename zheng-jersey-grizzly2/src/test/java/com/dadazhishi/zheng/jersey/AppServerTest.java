package com.dadazhishi.zheng.jersey;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import com.dadazhishi.zheng.jersey.configuration.JerseyConfiguration;
import com.dadazhishi.zheng.jersey.grizzly2.GrizzlyAppServerModule;
import com.google.inject.Guice;
import com.google.inject.Module;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import me.alexpanov.net.FreePortFinder;
import org.junit.Before;
import org.junit.Test;

public class AppServerTest {

  int port = 8080;
  AppServer appServer;
  JerseyConfiguration configuration;

  @Before
  public void setup() {
    port = FreePortFinder.findFreeLocalPort();

    configuration = JerseyConfiguration.builder()
        .addResourceClass(TestResource.class)
        .addPort(port)
        .withContextPath("/")
        .build();
    List<Module> modules = new ArrayList<>();
    modules.add(new AppServerModule(configuration));
    modules.add(new GrizzlyAppServerModule());
    modules.add(new MyModule());

    appServer = Guice.createInjector(modules)
        .getInstance(AppServer.class);
  }

  @Test
  public void start() throws Exception {
    try {
      appServer.start();
      Client client = ClientBuilder.newClient();
      WebTarget target = client.target("http://localhost:" + port)
          .path(configuration.getContextPath());
      String response = target.path(TestResource.PATH).request().get().readEntity(String.class);
      assertEquals(TestResource.MESSAGE, response);
    } finally {
      appServer.stop();
    }
  }

  @Test
  public void testInject() throws Exception {
    try {
      appServer.start();
      Client client = ClientBuilder.newClient();
      WebTarget target = client.target("http://localhost:" + port)
          .path(configuration.getContextPath());
      String response1 = target.path(TestResource.PATH + "/inject").request().get()
          .readEntity(String.class);
      String response2 = target.path(TestResource.PATH + "/inject").request().get()
          .readEntity(String.class);
      assertNotEquals(response1, response2);
    } finally {
      appServer.stop();
    }
  }

}