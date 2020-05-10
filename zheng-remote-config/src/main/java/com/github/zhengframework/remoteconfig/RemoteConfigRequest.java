package com.github.zhengframework.remoteconfig;

/*-
 * #%L
 * zheng-remote-config
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@ToString
public final class RemoteConfigRequest {

  private String[] configKeys;
  private List<ConfigParam> configParams;

  public RemoteConfigRequest(String[] configKeys, List<ConfigParam> configParams) {
    Objects.requireNonNull(configKeys, "configKeys is null");
    this.configKeys = Arrays.copyOf(configKeys, configKeys.length);
    this.configParams = Collections.unmodifiableList(configParams);
  }

  public static RemoteConfigRequestBuilder builder() {
    return new RemoteConfigRequestBuilder();
  }

  public String[] getConfigKeys() {
    return Arrays.copyOf(configKeys, configKeys.length);
  }

  public void setConfigKeys(String[] configKeys) {
    Objects.requireNonNull(configKeys, "configKeys is null");
    this.configKeys = Arrays.copyOf(configKeys, configKeys.length);
  }

  public List<ConfigParam> getConfigParams() {
    return Collections.unmodifiableList(configParams);
  }

  public void setConfigParams(List<ConfigParam> configParams) {
    this.configParams = configParams;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RemoteConfigRequest that = (RemoteConfigRequest) o;
    return Arrays.equals(getConfigKeys(), that.getConfigKeys())
        && getConfigParams().equals(that.getConfigParams());
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(getConfigParams());
    result = 31 * result + Arrays.hashCode(getConfigKeys());
    return result;
  }

  public static final class RemoteConfigRequestBuilder {

    private String[] configKeys;
    private List<ConfigParam> configParams;

    private RemoteConfigRequestBuilder() {}

    public RemoteConfigRequestBuilder configKeys(String... configKeys) {
      this.configKeys = Objects.requireNonNull(configKeys, "configKeys is null");
      return this;
    }

    public RemoteConfigRequestBuilder configParams(List<ConfigParam> configParams) {
      if (this.configParams == null) {
        this.configParams = new ArrayList<>();
      }
      this.configParams.addAll(Objects.requireNonNull(configParams, "configParams is null"));
      return this;
    }

    public RemoteConfigRequestBuilder configParams(ConfigParam... configParams) {
      if (this.configParams == null) {
        this.configParams = new ArrayList<>();
      }
      this.configParams.addAll(
          Arrays.asList(Objects.requireNonNull(configParams, "configParams is null")));
      return this;
    }

    public RemoteConfigRequest build() {
      return new RemoteConfigRequest(
          Optional.ofNullable(configKeys).orElse(new String[0]),
          Optional.ofNullable(configParams).orElse(new ArrayList<>()));
    }
  }
}
