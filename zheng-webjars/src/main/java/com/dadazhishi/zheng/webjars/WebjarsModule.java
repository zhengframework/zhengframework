package com.dadazhishi.zheng.webjars;

import com.dadazhishi.zheng.configuration.Configuration;
import com.dadazhishi.zheng.configuration.ConfigurationObjectMapper;
import com.dadazhishi.zheng.configuration.ConfigurationSupport;
import com.google.inject.servlet.ServletModule;
import java.util.Collections;
import java.util.Map;
import javax.inject.Singleton;

public class WebjarsModule extends ServletModule implements ConfigurationSupport {

  private Configuration configuration;

  @Override
  protected void configureServlets() {
    WebjarsConfig webjarsConfig = ConfigurationObjectMapper
        .resolve(configuration, WebjarsConfig.NAMESPACE, WebjarsConfig.class);
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
  public void setConfiguration(Configuration configuration) {
    this.configuration = configuration;
  }
}
