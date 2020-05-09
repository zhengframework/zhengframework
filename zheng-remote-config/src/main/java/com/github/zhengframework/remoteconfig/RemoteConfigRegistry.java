package com.github.zhengframework.remoteconfig;

import java.util.Collection;

public interface RemoteConfigRegistry {

  Collection<RemoteConfig> getRemoteConfigs();
}
