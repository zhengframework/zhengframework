package com.github.zhengframework.webjars;

import com.github.zhengframework.configuration.ConfigurationAwareServletModule;
import com.github.zhengframework.configuration.ConfigurationBeanMapper;
import com.github.zhengframework.web.PathUtils;
import java.util.Collections;
import java.util.Map;
import javax.inject.Singleton;

public class WebjarsModule extends ConfigurationAwareServletModule {

  @Override
  protected void configureServlets() {
    Map<String, WebjarsConfig> configMap = ConfigurationBeanMapper
        .resolve(getConfiguration(), WebjarsConfig.class);
    WebjarsConfig webjarsConfig = configMap.getOrDefault("", new WebjarsConfig());
    bind(WebjarsConfig.class).toInstance(webjarsConfig);
    bind(WebjarsServlet.class).in(Singleton.class);
    String path = webjarsConfig.getBasePath();
    path = PathUtils.fixPath(path);
    Map<String, String> initParams = Collections
        .singletonMap("disableCache", "" + webjarsConfig.isDisableCache());
    if (path == null) {
      serve("/webjars/*").with(WebjarsServlet.class, initParams);
    } else {
      serve(path + "/*").with(WebjarsServlet.class, initParams);
    }
  }

}
