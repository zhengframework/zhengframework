package com.github.zhengframework.web.undertow;

/*-
 * #%L
 * zheng-web-undertow
 * %%
 * Copyright (C) 2020 Zheng MingHai
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import io.undertow.server.handlers.resource.Resource;
import io.undertow.server.handlers.resource.ResourceChangeListener;
import io.undertow.server.handlers.resource.ResourceManager;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

public class ResourceManagerCollection implements ResourceManager {

  private final ResourceManager[] resourceManagers;

  public ResourceManagerCollection(ResourceManager... resourceManagers) {
    ResourceManager[] resourceManagers1 = Objects.requireNonNull(resourceManagers);
    this.resourceManagers = Arrays.copyOf(resourceManagers1, resourceManagers1.length);
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
