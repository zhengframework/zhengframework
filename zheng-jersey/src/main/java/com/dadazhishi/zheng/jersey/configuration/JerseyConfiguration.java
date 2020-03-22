package com.dadazhishi.zheng.jersey.configuration;

import java.util.List;
import java.util.Objects;
import org.glassfish.jersey.server.ResourceConfig;

public class JerseyConfiguration {

  private final List<ServerConnectorConfiguration> serverConnectors;
  private final ResourceConfig resourceConfig;
  private final String contextPath;

  JerseyConfiguration(List<ServerConnectorConfiguration> serverConnectors,
      ResourceConfig resourceConfig,
      String contextPath) {
    this.serverConnectors = Objects.requireNonNull(serverConnectors);
    this.resourceConfig = Objects.requireNonNull(resourceConfig);
    this.contextPath = appendLeadingSlashIfMissing(contextPath);
    if (serverConnectors.size() == 0) {
      throw new RuntimeException("Must supply at least one server connector");
    }
  }

  public static JerseyConfigurationBuilder builder() {
    return new JerseyConfigurationBuilder();
  }

  public List<ServerConnectorConfiguration> getServerConnectors() {
    return serverConnectors;
  }

  public ResourceConfig getResourceConfig() {
    return resourceConfig;
  }

  public String getContextPath() {
    return contextPath;
  }

  private String appendLeadingSlashIfMissing(String contextRoot) {
    contextRoot = Objects.requireNonNull(contextRoot);
    if (!contextRoot.startsWith("/")) {
      contextRoot = "/" + contextRoot;
    }

    return contextRoot;
  }

}
