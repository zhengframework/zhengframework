package com.github.zhengframework.remoteconfig.servlet;

import com.github.zhengframework.configuration.ConfigurationAwareServletModule;
import com.github.zhengframework.configuration.ConfigurationBeanMapper;
import com.github.zhengframework.remoteconfig.RemoteConfigServerConfig;
import com.github.zhengframework.web.PathUtils;
import java.util.Map;
import javax.inject.Singleton;

public class RemoteConfigServletModule extends ConfigurationAwareServletModule {

  @Override
  protected void configureServlets() {
    Map<String, RemoteConfigServerConfig> configMap =
        ConfigurationBeanMapper.resolve(getConfiguration(), RemoteConfigServerConfig.class);
    RemoteConfigServerConfig remoteConfigServerConfig = configMap
        .getOrDefault("", new RemoteConfigServerConfig());
    if (remoteConfigServerConfig.isEnable()) {
      Map<String, RemoteConfigServerServletConfig> configMap2 =
          ConfigurationBeanMapper
              .resolve(getConfiguration(), RemoteConfigServerServletConfig.class);
      RemoteConfigServerServletConfig remoteConfigServerServletConfig = configMap2
          .getOrDefault("", new RemoteConfigServerServletConfig());
      bind(RemoteConfigServerServletConfig.class).toInstance(remoteConfigServerServletConfig);
      if (remoteConfigServerServletConfig.isEnable()) {
        bind(RemoteConfigServerServlet.class).in(Singleton.class);
        String path = remoteConfigServerServletConfig.getBasePath();
        path = PathUtils.fixPath(path);
        serve(path + "/*").with(RemoteConfigServerServlet.class);
      }
    }
  }

}
