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

import com.google.common.base.Preconditions;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public class CombinedLocationStrategy implements FileLocationStrategy {

  private final Collection<FileLocationStrategy> subStrategies;

  public CombinedLocationStrategy(
      Collection<FileLocationStrategy> subStrategies) {
    Preconditions
        .checkState(subStrategies != null, "Collection with sub strategies must not be null!");
    this.subStrategies = Collections.unmodifiableCollection(subStrategies);
  }

  @Override
  public Optional<URL> locate(FileSystem fileSystem, FileLocator locator) {
    for (final FileLocationStrategy sub : subStrategies) {
      Optional<URL> optional = sub.locate(fileSystem, locator);
      if (optional.isPresent()) {
        return optional;
      }
    }
    return Optional.empty();
  }
}
