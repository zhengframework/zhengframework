package com.github.zhengframework.remoteconfig;

import java.util.List;
import java.util.Map;

public interface RemoteConfigServer {

  void init();

  Map<String,RemoteConfigResponse<?>> getConfig(String[] configKeys,List<ConfigParam> configParams);
}
