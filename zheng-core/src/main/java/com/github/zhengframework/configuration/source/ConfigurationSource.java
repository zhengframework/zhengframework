package com.github.zhengframework.configuration.source;

/*-
 * #%L
 * zheng-core
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
import java.util.Map;

public interface ConfigurationSource {

  /**
   * @throws ConfigurationSourceException fetch fail
   * @throws IllegalStateException when source was improperly configured
   */
  void init();

  void addListener(ConfigurationSourceListener listener);

  void removeListener(ConfigurationSourceListener listener);

  /**
   * Get configuration
   *
   * @param environment environment to use
   * @return configuration set for {@code environment}
   * @throws MissingEnvironmentException when requested environment couldn't be found
   * @throws IllegalStateException when unable to fetch configuration
   */
  Map<String, String> getConfiguration(Environment environment);
}
