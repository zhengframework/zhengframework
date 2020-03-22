package com.dadazhishi.zheng.jersey.jetty;

import com.dadazhishi.zheng.jersey.AppServer;
import com.dadazhishi.zheng.jersey.configuration.JerseyConfiguration;
import com.dadazhishi.zheng.jersey.jetty.ServletContextHandlerConfigurer.DefaultServletContextHandlerConfigurer;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.multibindings.OptionalBinder;
import javax.inject.Singleton;
import org.eclipse.jetty.server.Server;

public class JettyAppServerModule extends AbstractModule {

  private Provider<Injector> injectorProvider;

  @Override
  protected void configure() {
    injectorProvider = getProvider(Injector.class);
    OptionalBinder.newOptionalBinder(binder(), ServletContextHandlerConfigurer.class).setDefault()
        .toInstance(new DefaultServletContextHandlerConfigurer());
    OptionalBinder.newOptionalBinder(binder(), JettyServerCreator.class).setDefault()
        .toInstance(Server::new);
  }

  @Singleton
  @Provides
  @Inject(optional = true)
  public AppServer appServer(JerseyConfiguration jerseyConfiguration,
      JettyServerCreator jettyServerCreator,
      ServletContextHandlerConfigurer servletContextHandlerConfigurer) {
    return new JettyAppServer(jerseyConfiguration, injectorProvider::get, jettyServerCreator,
        servletContextHandlerConfigurer);
  }
}
