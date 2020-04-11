package com.github.zhengframework.webjars;

import com.github.zhengframework.configuration.ConfigurationBeanMapper;
import com.github.zhengframework.core.Configuration;
import com.github.zhengframework.core.ConfigurationAware;
import com.github.zhengframework.web.PathUtils;
import com.github.zhengframework.web.WebModule;
import com.google.common.base.Preconditions;
import com.google.inject.servlet.ServletModule;
import java.util.Collections;
import java.util.Map;
import javax.inject.Singleton;

public class WebjarsModule extends ServletModule implements ConfigurationAware {

  private Configuration configuration;

  @Override
  protected void configureServlets() {
    Preconditions.checkArgument(configuration != null, "configuration is null");
    WebModule webModule = new WebModule();
    webModule.initConfiguration(configuration);
    install(webModule);
    WebjarsConfig webjarsConfig = ConfigurationBeanMapper
        .resolve(configuration, WebjarsConfig.PREFIX, WebjarsConfig.class);
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

  @Override
  public void initConfiguration(Configuration configuration) {
    this.configuration = configuration;
  }
}
