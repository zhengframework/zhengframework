package com.dadazhishi.zheng.rest.jersey;

import com.google.inject.Binding;
import com.google.inject.Injector;
import java.lang.reflect.Type;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.ws.rs.Path;
import javax.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.jvnet.hk2.guice.bridge.api.GuiceBridge;
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge;

@Slf4j
public class JerseyResourceConfig extends ResourceConfig {

  @Inject
  public JerseyResourceConfig(
      ServiceLocator serviceLocator,
      ServletContext servletContext) {
//    property(CommonProperties.METAINF_SERVICES_LOOKUP_DISABLE, true);
//    property(CommonProperties.FEATURE_AUTO_DISCOVERY_DISABLE, true);
//    property(CommonProperties.JSON_PROCESSING_FEATURE_DISABLE, true);
//    property(CommonProperties.MOXY_JSON_FEATURE_DISABLE, true);

    register(JacksonFeature.class);

    Injector injector = (Injector) servletContext.getAttribute(Injector.class.getName());
    GuiceBridge.getGuiceBridge().initializeGuiceBridge(serviceLocator);
    GuiceIntoHK2Bridge guiceBridge = serviceLocator.getService(GuiceIntoHK2Bridge.class);
    guiceBridge.bridgeGuiceInjector(injector);

    register(this, injector);
  }

  private void register(final ResourceConfig resourceConfig, final Injector injector) {

    for (final Binding<?> binding : injector.getBindings().values()) {
      final Type type = binding.getKey().getTypeLiteral().getType();
      if (type instanceof Class) {
        final Class<?> beanClass = (Class<?>) type;

        if (beanClass.isAnnotationPresent(Path.class) || beanClass
            .isAnnotationPresent(Provider.class)) {
          log.debug("Registering {}", beanClass);
          resourceConfig.register(beanClass);
        }
      }
    }
  }
}
