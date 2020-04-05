package com.dadazhishi.zheng.rest.jersey;

import com.google.inject.Injector;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.CommonProperties;
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
    property(CommonProperties.METAINF_SERVICES_LOOKUP_DISABLE, true);
    property(CommonProperties.FEATURE_AUTO_DISCOVERY_DISABLE, true);
    property(CommonProperties.JSON_PROCESSING_FEATURE_DISABLE, true);
    property(CommonProperties.MOXY_JSON_FEATURE_DISABLE, true);

    register(JacksonFeature.class);

    Injector injector = (Injector) servletContext.getAttribute(Injector.class.getName());
    GuiceBridge.getGuiceBridge().initializeGuiceBridge(serviceLocator);
    GuiceIntoHK2Bridge guiceBridge = serviceLocator.getService(GuiceIntoHK2Bridge.class);
    guiceBridge.bridgeGuiceInjector(injector);
    injector.getInstance(FeatureClassScanner.class).accept(component -> {
      log.info("register Feature {}", component.getClass());
      register(component);
    });
    injector.getInstance(PathAnnotationScanner.class).accept(component -> {
      log.info("register Path {}", component.getClass());
      register(component);
    });
    injector.getInstance(ProviderAnnotationScanner.class).accept(component -> {
      log.info("register Provider {}", component.getClass());
      register(component);
    });

  }

}
