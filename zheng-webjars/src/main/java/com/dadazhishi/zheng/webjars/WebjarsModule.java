package com.dadazhishi.zheng.webjars;

import com.dadazhishi.zheng.configuration.Configuration;
import com.dadazhishi.zheng.configuration.ConfigurationAware;
import com.dadazhishi.zheng.configuration.ConfigurationBeanMapper;
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
    WebjarsConfig webjarsConfig = ConfigurationBeanMapper
        .resolve(configuration, WebjarsConfig.PREFIX, WebjarsConfig.class);
    bind(WebjarsConfig.class).toInstance(webjarsConfig);
    bind(WebjarsServlet.class).in(Singleton.class);
    String path = webjarsConfig.getPath();
    if ("/".equals(path)) {
      path = null;
    } else {
      if (path.endsWith("/")) {
        path = path.substring(0, path.length() - 1);
      }
      if (!path.startsWith("/")) {
        path = "/" + path;
      }
    }
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
