package com.github.zhengframework.remoteconfig;

import java.util.List;

public class EchoRemoteConfig implements RemoteConfig {

  @Override
  public boolean supportKey(String key) {
    return "echo".equals(key);
  }

  @Override
  public int order() {
    return 0;
  }

  @Override
  public RemoteConfigResponse<?> get(String key, List<ConfigParam> configParams) throws Exception {
    return RemoteConfigResponse.builder().success(true).data(configParams).build();
  }
}
