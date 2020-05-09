package com.github.zhengframework.remoteconfig;

import java.util.List;

public class CustomThrowableRemoteConfig implements RemoteConfig {

  @Override
  public boolean supportKey(String key) {
    return "CustomThrowableRemoteConfig".equals(key);
  }

  @Override
  public int order() {
    return 0;
  }

  @Override
  public RemoteConfigResponse<?> get(String key, List<ConfigParam> configParams) throws Exception {
    throw new IllegalAccessException("CustomThrowableRemoteConfig");
  }
}
