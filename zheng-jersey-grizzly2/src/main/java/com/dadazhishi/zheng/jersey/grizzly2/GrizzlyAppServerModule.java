package com.dadazhishi.zheng.jersey.grizzly2;

import com.dadazhishi.zheng.jersey.AppServer;
import com.dadazhishi.zheng.jersey.configuration.JerseyConfiguration;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.multibindings.OptionalBinder;
import javax.inject.Singleton;
import org.glassfish.grizzly.http.server.HttpServer;

public class GrizzlyAppServerModule extends AbstractModule {

  private Provider<Injector> injectorProvider;

  @Override
  protected void configure() {
    injectorProvider = getProvider(Injector.class);
    OptionalBinder.newOptionalBinder(binder(), GrizzlyServerCreator.class).setDefault()
        .toInstance(HttpServer::new);
  }

  @Singleton
  @Provides
  @Inject(optional = true)
  public AppServer appServer(JerseyConfiguration jerseyConfiguration,
      GrizzlyServerCreator jettyServerCreator) {
    return new GrizzlyAppServer(jerseyConfiguration, injectorProvider::get, jettyServerCreator);
  }
}
