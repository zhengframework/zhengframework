package com.github.zhengframework.rest;

import com.github.zhengframework.configuration.Configuration;
import com.github.zhengframework.configuration.ConfigurationAwareServletModule;
import com.github.zhengframework.configuration.ConfigurationBeanMapper;
import com.github.zhengframework.web.PathUtils;
import com.github.zhengframework.web.WebConfig;
import com.google.inject.Scopes;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.plugins.interceptors.AcceptEncodingGZIPFilter;
import org.jboss.resteasy.plugins.interceptors.CacheControlFeature;
import org.jboss.resteasy.plugins.interceptors.GZIPDecodingInterceptor;
import org.jboss.resteasy.plugins.interceptors.GZIPEncodingInterceptor;
import org.jboss.resteasy.plugins.interceptors.ServerContentEncodingAnnotationFeature;
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;

@Slf4j
@EqualsAndHashCode(callSuper = false)
public class RestModule extends ConfigurationAwareServletModule {


  @Override
  protected void configureServlets() {
    install(new RequestScopeModule());
    Configuration configuration = getConfiguration();
    Map<String, WebConfig> webConfigMap = ConfigurationBeanMapper
        .resolve(configuration, WebConfig.class);
    WebConfig webConfig = webConfigMap.getOrDefault("", new WebConfig());

    Map<String, RestConfig> restConfigMap = ConfigurationBeanMapper
        .resolve(configuration, RestConfig.class);
    RestConfig restConfig = restConfigMap.getOrDefault("", new RestConfig());
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
      log.info("rest prefix path={}", hashMap.get("resteasy.servlet.mapping.prefix"));
    }
    log.info("rest path={}", path);
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

}
