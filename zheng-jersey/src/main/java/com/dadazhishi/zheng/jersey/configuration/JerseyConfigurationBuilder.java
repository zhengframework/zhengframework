package com.dadazhishi.zheng.jersey.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.mvc.MvcFeature;

public class JerseyConfigurationBuilder {

  private String contextPath;
  private Set<Class<?>> classes;
  private Map<String, Boolean> packages;
  private ResourceConfig resourceConfig;
  private Set<ServerConnectorConfiguration> connectors;

  JerseyConfigurationBuilder() {
    contextPath = "";
    connectors = new HashSet<>();
    packages = new HashMap<>();
    classes = new HashSet<>();
  }

  public JerseyConfigurationBuilder withContextPath(String contextPath) {
    this.contextPath = contextPath;
    return this;
  }

  public JerseyConfigurationBuilder withResourceConfig(ResourceConfig resourceConfig) {
    this.resourceConfig = resourceConfig;
    return this;
  }

  public JerseyConfigurationBuilder addPort(int port) {
    connectors.add(new ServerConnectorConfiguration(port));
    return this;
  }

  public JerseyConfigurationBuilder addHost(String host, int port) {
    connectors.add(new ServerConnectorConfiguration(host, port));
    return this;
  }

  public JerseyConfigurationBuilder addNamedHost(String name, String host, int port) {
    connectors.add(new ServerConnectorConfiguration(name, host, port));
    return this;
  }

  public JerseyConfigurationBuilder addPackage(boolean recursive, String packageToScan) {
    packages.put(packageToScan, recursive);
    return this;
  }

  public JerseyConfigurationBuilder addPackage(String packageToScan) {
    return addPackage(true, packageToScan);
  }

  public JerseyConfigurationBuilder addResourceClass(Class<?> resourceClass) {
    classes.add(resourceClass);
    return this;
  }

  public JerseyConfigurationBuilder registerClasses(Class<?> clazz) {
    classes.add(clazz);
    return this;
  }

  public JerseyConfiguration build() {
    if (resourceConfig == null) {
      resourceConfig = new ResourceConfig();
    }
    if (contextPath == null) {
      contextPath = "/";
    }

    resourceConfig.registerClasses(classes);
    tryRegisterClass("org.glassfish.jersey.jackson.JacksonFeature");
    tryRegisterClass("org.glassfish.jersey.media.multipart.MultiPartFeature");
    tryRegisterClass("org.glassfish.jersey.kryo.KryoFeature");
    tryRegisterClass("org.glassfish.jersey.jettison.JettisonFeature");
    tryRegisterClass("com.alibaba.fastjson.support.jaxrs.FastJsonFeature");

    tryRegisterClass("org.glassfish.jersey.media.sse.SseFeature");

    tryRegisterClass("org.glassfish.jersey.server.mvc.beanvalidation.MvcBeanValidationFeature");

    tryRegisterClass("org.glassfish.jersey.server.mvc.MvcFeature", resourceConfig -> {
      tryRegisterClass("org.glassfish.jersey.server.mvc.freemarker.FreemarkerMvcFeature");
      tryRegisterClass("org.glassfish.jersey.server.mvc.mustache.MustacheMvcFeature");
      resourceConfig.property(MvcFeature.TEMPLATE_BASE_PATH, "templates");
      resourceConfig.property(MvcFeature.ENCODING, "utf-8");
    });

    packages
        .forEach((packageToScan, recursive) -> resourceConfig.packages(recursive, packageToScan));

    return new JerseyConfiguration(new ArrayList<>(connectors), resourceConfig, contextPath);
  }

  public void tryRegisterClass(String className) {
    try {
      resourceConfig.register(Class.forName(className));
    } catch (ClassNotFoundException ignored) {
    }
  }

  public void tryRegisterClass(String className, ResourceConfigSetter resourceConfigSetter) {
    boolean success = true;
    try {
      resourceConfig.register(Class.forName(className));
    } catch (ClassNotFoundException e) {
      success = false;
    }
    if (success) {
      resourceConfigSetter.set(resourceConfig);
    }
  }

  interface ResourceConfigSetter {

    void set(ResourceConfig resourceConfig);
  }
}
