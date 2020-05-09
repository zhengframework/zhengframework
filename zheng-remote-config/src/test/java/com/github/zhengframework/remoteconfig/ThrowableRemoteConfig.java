package com.github.zhengframework.remoteconfig;

import java.util.List;

public class ThrowableRemoteConfig implements RemoteConfig {

  public static final String MSG = "ThrowableRemoteConfig";

  @Override
  public boolean supportKey(String key) {
    return "throwable".equals(key);
  }

  @Override
  public int order() {
    return 0;
  }

  @Override
  public RemoteConfigResponse<?> get(String key, List<ConfigParam> configParams) throws Exception {
    throw new RemoteConfigException(RemoteConfigResponse.builder().success(false)
        .message(MSG).build());
  }
}
