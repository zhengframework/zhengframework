package com.github.zhengframework.remoteconfig;

import com.google.common.collect.Lists;
import java.util.List;

public class MultiKeyRemoteConfig implements RemoteConfig {

  private List<String> list = Lists.newArrayList("key1", "key2");

  @Override
  public boolean supportKey(String key) {
    return list.contains(key);
  }

  @Override
  public int order() {
    return 0;
  }

  @Override
  public RemoteConfigResponse<?> get(String key, List<ConfigParam> configParams) throws Exception {
    return RemoteConfigResponse.builder().data("key").build();
  }
}
