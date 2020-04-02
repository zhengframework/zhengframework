package com.dadazhishi.zheng.rest.jersey;

import static com.dadazhishi.zheng.rest.RestConfig.NAMESPACE;

import com.dadazhishi.zheng.configuration.Configuration;
import com.dadazhishi.zheng.configuration.ConfigurationObjectMapper;
import com.dadazhishi.zheng.configuration.ConfigurationSupport;
import com.dadazhishi.zheng.rest.ObjectMapperContextResolver;
import com.dadazhishi.zheng.rest.RestConfig;
import com.dadazhishi.zheng.web.WebModule;
import com.google.inject.Injector;
import com.google.inject.Scopes;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import java.util.Collections;
import java.util.Map;
import javax.inject.Provider;
import javax.servlet.ServletContextListener;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.servlet.ServletContainer;

@Slf4j
@EqualsAndHashCode(callSuper = false, of = {})
public class RestModule extends ServletModule implements ConfigurationSupport {

  public static final String DEFAULT_RESOURCE_CONFIG_CLASS = JerseyResourceConfig.class.getName();
  private Provider<Injector> injectorProvider;
  private Configuration configuration;


  @Override
  public void setConfiguration(Configuration configuration) {
    this.configuration = configuration;
  }

  @Override
  protected void configureServlets() {
    injectorProvider = getProvider(Injector.class);
    install(new WebModule());
    bind(ServletContainer.class).in(Scopes.SINGLETON);
    bind(FeatureScanner.class);
    bind(PathScanner.class);
    bind(ProviderScanner.class);

    RestConfig restConfig = ConfigurationObjectMapper
        .resolve(configuration, NAMESPACE, RestConfig.class);
    bind(RestConfig.class).toInstance(restConfig);

    String resourceConfigClass = restConfig.getProperties()
        .getOrDefault("jersey.resourceConfigClass", DEFAULT_RESOURCE_CONFIG_CLASS);
    log.warn("cannot get key 'jersey.resourceConfigClass' from RestConfig, use default class [{}]",
        DEFAULT_RESOURCE_CONFIG_CLASS);

    Map<String, String> map = Collections
        .singletonMap("javax.ws.rs.Application", resourceConfigClass);

    String path = restConfig.getPath();
    if ("/".equals(path)) {
      path = null;
    }
    if (path != null && path.length() > 1 && path.endsWith("/")) {
      path = path.substring(0, path.length() - 1);
    }
    if (path == null) {
      serve("/*").with(ServletContainer.class, map);
    } else {
      serve(path + "/*").with(ServletContainer.class, map);
    }
    Multibinder.newSetBinder(binder(), ServletContextListener.class).addBinding()
        .toInstance(new GuiceServletContextListener() {
          @Override
          protected Injector getInjector() {
            return injectorProvider.get();
          }
        });

    bind(ObjectMapperContextResolver.class);
  }

}
