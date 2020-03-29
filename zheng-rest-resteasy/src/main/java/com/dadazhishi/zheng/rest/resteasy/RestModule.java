package com.dadazhishi.zheng.rest.resteasy;

import com.dadazhishi.zheng.web.WebModule;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Scopes;
import com.google.inject.servlet.ServletModule;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.plugins.guice.GuiceResteasyBootstrapServletContextListener;
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;

@Slf4j
@EqualsAndHashCode(callSuper = false, of = {})
public class RestModule extends ServletModule {

  private final String path;

  public RestModule() {
    this(null);
  }

  public RestModule(String path) {
    this.path = path;
  }

  @Override
  protected void configureServlets() {
    install(new WebModule());

    bind(GuiceResteasyBootstrapServletContextListener.class);
    bind(HttpServletDispatcher.class).in(Scopes.SINGLETON);

    if (path == null) {
      serve("/*").with(HttpServletDispatcher.class);
    } else {
      final Map<String, String> initParams = ImmutableMap
          .of("resteasy.servlet.mapping.prefix", path);
      serve(path + "/*").with(HttpServletDispatcher.class, initParams);
    }

    bind(ObjectMapperContextResolver.class);
  }

}
