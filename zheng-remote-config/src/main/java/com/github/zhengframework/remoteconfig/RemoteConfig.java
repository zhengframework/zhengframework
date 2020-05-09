package com.github.zhengframework.remoteconfig;

import java.util.List;

public interface RemoteConfig {

  boolean supportKey(String key);

  /**
   *
   * @return order [-1,0,1]
   */
  int order();

  RemoteConfigResponse<?> get(String key, List<ConfigParam> configParams) throws Exception;

}
