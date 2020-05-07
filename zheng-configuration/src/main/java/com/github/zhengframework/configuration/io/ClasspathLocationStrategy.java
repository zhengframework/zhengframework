package com.github.zhengframework.configuration.io;

/*-
 * #%L
 * zheng-configuration
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

import java.net.URL;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClasspathLocationStrategy implements FileLocationStrategy {

  @Override
  public Optional<URL> locate(FileSystem fileSystem, FileLocator locator) {
    ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
    URL resource = contextClassLoader.getResource(locator.getFileName());
    if (resource != null) {
      log.debug("Loading configuration from the context classpath ({})", locator.getFileName());
      return Optional.of(resource);
    }
    ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
    resource = systemClassLoader.getResource(locator.getFileName());
    if (resource != null) {
      log.debug("Loading configuration from the system classpath ({})", locator.getFileName());
      return Optional.of(resource);
    }
    return Optional.empty();
  }
}
