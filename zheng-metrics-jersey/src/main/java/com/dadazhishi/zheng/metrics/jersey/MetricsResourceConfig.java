package com.dadazhishi.zheng.metrics.jersey;

import com.dadazhishi.zheng.rest.jersey.JerseyResourceConfig;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletContext;
import org.glassfish.hk2.api.InterceptionService;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

public class MetricsResourceConfig extends JerseyResourceConfig {

  @Inject
  public MetricsResourceConfig(ServiceLocator serviceLocator,
      ServletContext servletContext) {
    super(serviceLocator, servletContext);
    register(new AbstractBinder() {
      @Override
      protected void configure() {
        bind(HK2InterceptionService.class).to(InterceptionService.class).in(Singleton.class);
      }
    });
  }
}
