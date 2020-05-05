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
import java.util.Collections;
import java.util.Map;

public class EmptyConfigurationSource implements ConfigurationSource {

  private final Map<String, String> map = Collections.emptyMap();

  @Override
  public void init() {

  }

  @Override
  public void addListener(ConfigurationSourceListener listener) {

  }

  @Override
  public void removeListener(ConfigurationSourceListener listener) {

  }

  @Override
  public Map<String, String> getConfiguration(Environment environment) {
    return map;
  }
}
