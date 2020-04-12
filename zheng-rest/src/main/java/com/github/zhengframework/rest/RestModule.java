package com.github.zhengframework.rest;

import static com.github.zhengframework.rest.RestConfig.PREFIX;

import com.github.zhengframework.configuration.Configuration;
import com.github.zhengframework.configuration.ConfigurationAware;
import com.github.zhengframework.configuration.ConfigurationBeanMapper;
import com.github.zhengframework.web.WebModule;
import com.google.common.base.Preconditions;
import com.google.inject.Scopes;
import com.google.inject.servlet.ServletModule;
import java.util.HashMap;
import java.util.Map.Entry;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.plugins.guice.GuiceResteasyBootstrapServletContextListener;
import org.jboss.resteasy.plugins.interceptors.AcceptEncodingGZIPFilter;
import org.jboss.resteasy.plugins.interceptors.CacheControlFeature;
import org.jboss.resteasy.plugins.interceptors.GZIPDecodingInterceptor;
import org.jboss.resteasy.plugins.interceptors.GZIPEncodingInterceptor;
import org.jboss.resteasy.plugins.interceptors.ServerContentEncodingAnnotationFeature;
import org.jboss.resteasy.plugins.providers.ByteArrayProvider;
import org.jboss.resteasy.plugins.providers.DefaultBooleanWriter;
import org.jboss.resteasy.plugins.providers.DefaultNumberWriter;
import org.jboss.resteasy.plugins.providers.DefaultTextPlain;
import org.jboss.resteasy.plugins.providers.FileProvider;
import org.jboss.resteasy.plugins.providers.FileRangeWriter;
import org.jboss.resteasy.plugins.providers.InputStreamProvider;
import org.jboss.resteasy.plugins.providers.StreamingOutputProvider;
import org.jboss.resteasy.plugins.providers.StringTextStar;
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
    install(new RequestScopeModule());

    bind(AcceptEncodingGZIPFilter.class);
    bind(GZIPDecodingInterceptor.class);
    bind(GZIPEncodingInterceptor.class);

    bind(InputStreamProvider.class);
    bind(ByteArrayProvider.class);
    bind(DefaultBooleanWriter.class);
    bind(DefaultNumberWriter.class);
    bind(DefaultTextPlain.class);
    bind(FileProvider.class);
    bind(FileRangeWriter.class);
    bind(StringTextStar.class);
    bind(StreamingOutputProvider.class);

    bind(ResteasyJackson2Provider.class);
    bind(ObjectMapperContextResolver.class);

    bind(CacheControlFeature.class);
    bind(ServerContentEncodingAnnotationFeature.class);

    bind(JsonMappingExceptionMapper.class);
    bind(DefaultGenericExceptionMapper.class);

    bind(GuiceResteasyBootstrapServletContextListener.class);
    bind(HttpServletDispatcher.class).in(Scopes.SINGLETON);

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
