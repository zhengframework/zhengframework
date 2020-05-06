package com.github.zhengframework.configuration.environment;

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

import java.util.Objects;

public class ImmutableEnvironment implements Environment {

  private final String envName;

  public ImmutableEnvironment(String envName) {
    this.envName = envName;
  }

  @Override
  public String getName() {
    return envName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ImmutableEnvironment that = (ImmutableEnvironment) o;
    return envName.equals(that.envName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(envName);
  }
}
