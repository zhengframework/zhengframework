package com.dadazhishi.zheng.web;

import com.dadazhishi.zheng.service.ServicesModule;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.servlet.ServletModule;
import java.util.Iterator;
import java.util.ServiceLoader;
import javax.inject.Singleton;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = false, of = {})
public class WebModule extends ServletModule {

  @Override
  protected void configureServlets() {
    install(new ServicesModule());
    bind(WebServerService.class).asEagerSingleton();
  }

  @Singleton
  @Provides
  public WebServer webServer(Provider<WebConfig> webConfigProvider,
      Provider<Injector> injectorProvider) {
    ServiceLoader<WebServer> webServers = ServiceLoader.load(WebServer.class);
    Iterator<WebServer> iterator = webServers.iterator();
    if (iterator.hasNext()) {
      WebServer webServer = iterator.next();
      webServer.init(webConfigProvider, injectorProvider);
      return webServer;
    } else {
      throw new IllegalStateException("need WebServer");
    }
  }
}
