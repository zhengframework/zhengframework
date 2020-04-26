package com.github.zhengframework.web.undertow;

import io.undertow.server.handlers.resource.Resource;
import io.undertow.server.handlers.resource.ResourceChangeListener;
import io.undertow.server.handlers.resource.ResourceManager;
import java.io.IOException;
import java.util.Objects;

public class ResourceManagerCollection implements ResourceManager {

  private final ResourceManager[] resourceManagers;

  public ResourceManagerCollection(
      ResourceManager... resourceManagers) {
    this.resourceManagers = Objects.requireNonNull(resourceManagers);
  }

  @Override
  public Resource getResource(String s) throws IOException {
    for (ResourceManager resourceManager : resourceManagers) {
      Resource resource = resourceManager.getResource(s);
      if (resource != null) {
        return resource;
      }
    }
    return null;
  }

  @Override
  public boolean isResourceChangeListenerSupported() {
    return true;
  }

  @Override
  public void registerResourceChangeListener(ResourceChangeListener resourceChangeListener) {
    for (ResourceManager resourceManager : resourceManagers) {
      resourceManager.registerResourceChangeListener(resourceChangeListener);
    }
  }

  @Override
  public void removeResourceChangeListener(ResourceChangeListener resourceChangeListener) {
    for (ResourceManager resourceManager : resourceManagers) {
      resourceManager.removeResourceChangeListener(resourceChangeListener);
    }
  }

  @Override
  public void close() throws IOException {
    for (ResourceManager resourceManager : resourceManagers) {
      resourceManager.close();
    }
  }
}
