package com.dadazhishi.zheng.rest.resteasy;

import static com.dadazhishi.zheng.rest.RestConfig.PREFIX;

import com.dadazhishi.zheng.configuration.Configuration;
import com.dadazhishi.zheng.configuration.ConfigurationBeanMapper;
import com.dadazhishi.zheng.configuration.ConfigurationSupport;
import com.dadazhishi.zheng.rest.ObjectMapperContextResolver;
import com.dadazhishi.zheng.rest.RestConfig;
import com.dadazhishi.zheng.web.WebModule;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Scopes;
import com.google.inject.servlet.ServletModule;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.plugins.guice.GuiceResteasyBootstrapServletContextListener;
import org.jboss.resteasy.plugins.providers.jackson.ResteasyJackson2Provider;
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;

@Slf4j
@EqualsAndHashCode(callSuper = false, of = {})
public class RestModule extends ServletModule implements ConfigurationSupport {

  private Configuration configuration;


  @Override
  protected void configureServlets() {
    install(new WebModule());

    bind(ResteasyJackson2Provider.class);
    bind(GuiceResteasyBootstrapServletContextListener.class);
    bind(HttpServletDispatcher.class).in(Scopes.SINGLETON);
    bind(ObjectMapperContextResolver.class);

    RestConfig restConfig = ConfigurationBeanMapper
        .resolve(configuration, PREFIX, RestConfig.class);
    bind(RestConfig.class).toInstance(restConfig);
    String path = restConfig.getPath();
    if ("/".equals(path)) {
      path = null;
    }
    if (path != null && path.length() > 1 && path.endsWith("/")) {
      path = path.substring(0, path.length() - 1);
    }
    if (path == null) {
      serve("/*").with(HttpServletDispatcher.class);
    } else {
      final Map<String, String> initParams = ImmutableMap
          .of("resteasy.servlet.mapping.prefix", path);
      serve(path + "/*").with(HttpServletDispatcher.class, initParams);
    }
  }

  @Override
  public void setConfiguration(Configuration configuration) {
    this.configuration = configuration;
  }
}
