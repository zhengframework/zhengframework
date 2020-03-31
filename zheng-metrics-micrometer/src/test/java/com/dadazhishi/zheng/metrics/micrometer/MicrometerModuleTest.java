package com.dadazhishi.zheng.metrics.micrometer;


import com.dadazhishi.zheng.rest.jersey.RestModule;
import com.dadazhishi.zheng.service.Run;
import com.dadazhishi.zheng.web.WebConfig;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.micrometer.core.annotation.Counted;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

public class MicrometerModuleTest {

  public static void main(String[] args) throws Exception {
    final Injector injector = Guice.createInjector(
        new MicrometerModule()
        , new RestModule()
        , new AbstractModule() {
          @Override
          protected void configure() {
            bind(TestResource.class);
            bind(WebConfig.class).toInstance(new WebConfig());
          }
        }
    );

    // start services
    injector.getInstance(Run.class).start();
  }

  @Path("/test")
  public static class TestResource {

    @GET
    @Counted
    public String hello() {
      return "hello " + System.currentTimeMillis() + "\n";
    }
  }
}