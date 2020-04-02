package com.dadazhishi.zheng.web;

import com.dadazhishi.zheng.configuration.Configuration;
import com.dadazhishi.zheng.configuration.ConfigurationObjectMapper;
import com.dadazhishi.zheng.configuration.ConfigurationSupport;
import com.dadazhishi.zheng.service.ServicesModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.servlet.ServletModule;
import java.util.Iterator;
import java.util.ServiceLoader;
import javax.inject.Provider;
import javax.inject.Singleton;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = false, of = {})
public class WebModule extends ServletModule implements ConfigurationSupport {

  private Configuration configuration;

  @Override
  public void setConfiguration(Configuration configuration) {
    this.configuration = configuration;
  }

  @Override
  protected void configureServlets() {
    install(new ServicesModule());
    bind(WebServerService.class).asEagerSingleton();
    WebConfig webConfig = ConfigurationObjectMapper
        .resolve(configuration, WebConfig.NAMESPACE, WebConfig.class);
    bind(WebConfig.class).toInstance(webConfig);
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