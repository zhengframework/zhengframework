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

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultRemoteConfigServer implements RemoteConfigServer {

  public static final NotFound NOT_FOUND = new NotFound();
  private final RemoteConfigExceptionMapper remoteConfigExceptionMapper;
  private List<RemoteConfig> remoteConfigs = new ArrayList<>();
  private RemoteConfigRegistry registry;

  @Inject
  public DefaultRemoteConfigServer(
      RemoteConfigExceptionMapper remoteConfigExceptionMapper, RemoteConfigRegistry registry) {
    this.remoteConfigExceptionMapper = remoteConfigExceptionMapper;
    this.registry = registry;
    init();
  }

  @Override
  public void init() {
    remoteConfigs.clear();
    remoteConfigs.addAll(ImmutableList.copyOf(registry.getRemoteConfigs()));
  }

  @Override
  public Map<String, RemoteConfigResponse<?>> getConfig(RemoteConfigRequest request) {
    String[] keys = request.getConfigKeys();
    List<ConfigParam> configParamList = request.getConfigParams();
    Map<String, RemoteConfigResponse<?>> map = new HashMap<>();

    for (String key : keys) {
      boolean found = false;
      ImmutableList<ConfigParam> configParams = ImmutableList.copyOf(configParamList);
      for (RemoteConfig remoteConfig : remoteConfigs) {
        if (remoteConfig.supportKey(key)) {
          try {
            map.put(key, remoteConfig.get(key, configParams));
          } catch (Exception e) {
            map.put(key, remoteConfigExceptionMapper.resolve(e));
          }
          found = true;
          break;
        }
      }
      if (!found) {
        map.put(key, NOT_FOUND.get(key, configParams));
      }
    }
    return map;
  }

  public static final class NotFound implements RemoteConfig {

    @Override
    public boolean supportKey(String key) {
      return true;
    }

    @Override
    public int order() {
      return Integer.MAX_VALUE;
    }

    @Override
    public RemoteConfigResponse<?> get(String key, List<ConfigParam> configParams) {
      return RemoteConfigResponse.builder()
          .success(false)
          .message(String.format(Locale.ENGLISH, "'%s' not found", key))
          .build();
    }
  }
}
