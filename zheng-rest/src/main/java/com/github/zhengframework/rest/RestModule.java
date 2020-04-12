package com.github.zhengframework.rest;

import static com.github.zhengframework.rest.RestConfig.PREFIX;

import com.github.zhengframework.configuration.Configuration;
import com.github.zhengframework.configuration.ConfigurationAware;
import com.github.zhengframework.configuration.ConfigurationBeanMapper;
import com.github.zhengframework.web.PathUtils;
import com.github.zhengframework.web.WebConfig;
import com.github.zhengframework.web.WebModule;
import com.google.common.base.Preconditions;
import com.google.inject.Scopes;
import com.google.inject.servlet.ServletModule;
import java.util.HashMap;
import java.util.Map;
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
    Map<String, WebConfig> webConfigMap = ConfigurationBeanMapper
        .resolve(configuration, WebConfig.class);
    WebConfig webConfig = webConfigMap.getOrDefault("", new WebConfig());

    Map<String, RestConfig> restConfigMap = ConfigurationBeanMapper
        .resolve(configuration, RestConfig.class);
    RestConfig restConfig = restConfigMap.getOrDefault("",new RestConfig());
    bind(RestConfig.class).toInstance(restConfig);
    String path = PathUtils.fixPath(
//        webConfig.getContextPath(),
        restConfig.getPath());
    HashMap<String, String> hashMap = new HashMap<>();
    for (Entry<String, String> entry : restConfig.getProperties().entrySet()) {
      if (entry.getKey().startsWith("resteasy.")) {
        hashMap.put(entry.getKey(), entry.getValue());
      }
    }
    if (!hashMap.containsKey("resteasy.servlet.mapping.prefix")) {
      hashMap.put("resteasy.servlet.mapping.prefix", PathUtils.fixPath(
//          webConfig.getContextPath(),
          restConfig.getPath()));
      log.info("rest prefix path={}",hashMap.get("resteasy.servlet.mapping.prefix"));
    }
    log.info("rest path={}",path);
    serve(path + "/*").with(HttpServletDispatcher.class, hashMap);
    bind(HttpServletDispatcher.class).in(Scopes.SINGLETON);

    bind(AcceptEncodingGZIPFilter.class);
    bind(GZIPDecodingInterceptor.class);
    bind(GZIPEncodingInterceptor.class);

    bind(ObjectMapperContextResolver.class);

    bind(CacheControlFeature.class);
    bind(ServerContentEncodingAnnotationFeature.class);

    bind(JsonMappingExceptionMapper.class);
    bind(DefaultGenericExceptionMapper.class);

    // bind(GuiceResteasyBootstrapServletContextListener.class);
    // replace custom @PostConstruct and @PreDestroy by LifecycleModule
    bind(ResteasyBootstrapServletContextListener.class);

  }

  @Override
  public void initConfiguration(Configuration configuration) {
    this.configuration = configuration;
  }
}
