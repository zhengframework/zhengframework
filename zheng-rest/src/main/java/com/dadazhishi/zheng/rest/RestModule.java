package com.dadazhishi.zheng.rest;

import static com.dadazhishi.zheng.rest.RestConfig.PREFIX;

import com.dadazhishi.zheng.configuration.Configuration;
import com.dadazhishi.zheng.configuration.ConfigurationAware;
import com.dadazhishi.zheng.configuration.ConfigurationBeanMapper;
import com.dadazhishi.zheng.web.WebModule;
import com.google.common.base.Preconditions;
import com.google.inject.Scopes;
import com.google.inject.servlet.ServletModule;
import java.util.HashMap;
import java.util.Map.Entry;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.plugins.guice.GuiceResteasyBootstrapServletContextListener;
import org.jboss.resteasy.plugins.interceptors.AcceptEncodingGZIPFilter;
import org.jboss.resteasy.plugins.interceptors.GZIPDecodingInterceptor;
import org.jboss.resteasy.plugins.interceptors.GZIPEncodingInterceptor;
import org.jboss.resteasy.plugins.providers.jackson.ResteasyJackson2Provider;
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;

@Slf4j
@EqualsAndHashCode(callSuper = false, of = {})
public class RestModule extends ServletModule implements ConfigurationAware {

  private Configuration configuration;


  @Override
  protected void configureServlets() {
    Preconditions.checkArgument(configuration != null, "configuration is null");
    WebModule webModule = new WebModule();
    webModule.initConfiguration(configuration);
    install(webModule);

    bind(AcceptEncodingGZIPFilter.class);
    bind(GZIPDecodingInterceptor.class);
    bind(GZIPEncodingInterceptor.class);
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
      HashMap<String, String> hashMap = new HashMap<>();
      for (Entry<String, String> entry : restConfig.getProperties().entrySet()) {
        if (entry.getKey().startsWith("resteasy.")) {
          hashMap.put(entry.getKey(), entry.getValue());
        }
      }
      if (!hashMap.containsKey("resteasy.servlet.mapping.prefix")) {
        hashMap.put("resteasy.servlet.mapping.prefix", path);
      }
      serve(path + "/*").with(HttpServletDispatcher.class, hashMap);
    }
  }

  @Override
  public void initConfiguration(Configuration configuration) {
    this.configuration = configuration;
  }
}