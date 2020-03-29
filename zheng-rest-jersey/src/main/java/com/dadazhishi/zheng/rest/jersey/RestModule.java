package com.dadazhishi.zheng.rest.jersey;

import com.dadazhishi.zheng.web.WebModule;
import com.google.inject.Injector;
import com.google.inject.Scopes;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import java.util.Collections;
import java.util.Map;
import javax.servlet.ServletContextListener;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.servlet.ServletContainer;

@Slf4j
@EqualsAndHashCode(callSuper = false, of = {})
public class RestModule extends ServletModule {

  private final String path;
  private final Class<? extends JerseyResourceConfig> resourceConfigClass;
  private javax.inject.Provider<Injector> injectorProvider;

  public RestModule() {
    this(JerseyResourceConfig.class, null);
  }

  public RestModule(String path) {
    this(JerseyResourceConfig.class, path);
  }

  public RestModule(
      Class<? extends JerseyResourceConfig> resourceConfigClass, String path) {
    this.resourceConfigClass = resourceConfigClass;
    this.path = path;
  }

  @Override
  protected void configureServlets() {
    injectorProvider = getProvider(Injector.class);
    install(new WebModule());
    bind(ServletContainer.class).in(Scopes.SINGLETON);
    Map<String, String> map = Collections
        .singletonMap("javax.ws.rs.Application", resourceConfigClass.getName());

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
