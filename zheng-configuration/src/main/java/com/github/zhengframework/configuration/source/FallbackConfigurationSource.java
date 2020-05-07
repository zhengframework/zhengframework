package com.github.zhengframework.configuration.source;

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

import com.github.zhengframework.configuration.environment.Environment;
import com.github.zhengframework.configuration.ex.ConfigurationSourceException;
import com.github.zhengframework.configuration.ex.MissingEnvironmentException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FallbackConfigurationSource implements ConfigurationSource {

  private final List<ConfigurationSource> sources = new ArrayList<>();

  public FallbackConfigurationSource(ConfigurationSource... sources) {
    Objects.requireNonNull(sources);
    for (ConfigurationSource source : sources) {
      this.sources.add(Objects.requireNonNull(source));
    }
  }

  public FallbackConfigurationSource(Iterator<ConfigurationSource> sources) {
    Objects.requireNonNull(sources)
        .forEachRemaining(
            configurationSource -> {
              FallbackConfigurationSource.this.sources.add(
                  Objects.requireNonNull(configurationSource));
            });
  }

  public FallbackConfigurationSource(Iterable<ConfigurationSource> sources) {
    Objects.requireNonNull(sources)
        .forEach(
            configurationSource -> {
              FallbackConfigurationSource.this.sources.add(
                  Objects.requireNonNull(configurationSource));
            });
  }

  @Override
  public void init() {
    boolean atLeastOneSuccess = false;
    for (ConfigurationSource source : sources) {
      try {
        source.init();
        atLeastOneSuccess = true;
      } catch (IllegalStateException | ConfigurationSourceException e) {
        // NOP
      }
    }
    if (!atLeastOneSuccess) {
      throw new IllegalStateException("Unable to initialize any of the underlying sources");
    }
  }

  @Override
  public void addListener(ConfigurationSourceListener listener) {
    for (ConfigurationSource source : sources) {
      source.addListener(listener);
    }
  }

  @Override
  public void removeListener(ConfigurationSourceListener listener) {
    for (ConfigurationSource source : sources) {
      source.removeListener(listener);
    }
  }

  @Override
  public Map<String, String> getConfiguration(Environment environment) {
    boolean allMissEnvironment = true;
    for (ConfigurationSource source : sources) {
      try {
        return source.getConfiguration(environment);
      } catch (MissingEnvironmentException e) {
        // NOP
      } catch (IllegalStateException e) {
        allMissEnvironment = false;
      }
    }
    if (allMissEnvironment) {
      throw new MissingEnvironmentException(environment.getName());
    }
    throw new IllegalStateException();
  }
}
