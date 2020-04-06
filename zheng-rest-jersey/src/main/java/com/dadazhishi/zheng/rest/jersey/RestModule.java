package com.dadazhishi.zheng.rest.jersey;

import static com.dadazhishi.zheng.rest.RestConfig.PREFIX;

import com.dadazhishi.zheng.configuration.Configuration;
import com.dadazhishi.zheng.configuration.ConfigurationAware;
import com.dadazhishi.zheng.configuration.ConfigurationBeanMapper;
import com.dadazhishi.zheng.rest.ObjectMapperContextResolver;
import com.dadazhishi.zheng.rest.RestConfig;
import com.dadazhishi.zheng.web.PathUtils;
import com.dadazhishi.zheng.web.WebModule;
import com.google.common.base.Preconditions;
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

/**
 * because jersey use hk2, when use guice, sometime has mistake.
 */
@Deprecated
@Slf4j
@EqualsAndHashCode(callSuper = false, of = {})
public class RestModule extends ServletModule implements ConfigurationAware {

  public static final String DEFAULT_RESOURCE_CONFIG_CLASS = JerseyResourceConfig.class.getName();
  private Provider<Injector> injectorProvider;
  private Configuration configuration;


  @Override
  public void initConfiguration(Configuration configuration) {
    this.configuration = configuration;
  }

  @Override
  protected void configureServlets() {
    Preconditions.checkArgument(configuration != null, "configuration is null");

    WebModule webModule = new WebModule();
    webModule.initConfiguration(configuration);
    install(webModule);
    bind(ServletContainer.class).in(Scopes.SINGLETON);
    bind(FeatureClassScanner.class);
    bind(PathAnnotationScanner.class);
    bind(ProviderAnnotationScanner.class);

    RestConfig restConfig = ConfigurationBeanMapper
        .resolve(configuration, PREFIX, RestConfig.class);
    bind(RestConfig.class).toInstance(restConfig);

    String resourceConfigClass = restConfig.getProperties()
        .getOrDefault("jersey.resourceConfigClass", DEFAULT_RESOURCE_CONFIG_CLASS);
    log.info("cannot get key 'jersey.resourceConfigClass' from RestConfig, use default class [{}]",
        DEFAULT_RESOURCE_CONFIG_CLASS);

    Map<String, String> map = Collections
        .singletonMap("javax.ws.rs.Application", resourceConfigClass);


    String path = restConfig.getPath();
    path = PathUtils.fixPath(path);
    if (path == null) {
      serve("/*").with(ServletContainer.class, map);
    } else {
      serve(path + "/*").with(ServletContainer.class, map);
    }
    injectorProvider = getProvider(Injector.class);
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
