package com.github.zhengframework.remoteconfig;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import javax.inject.Inject;

public class DefaultRemoteConfigRegistry implements RemoteConfigRegistry {

  private List<RemoteConfig> remoteConfigs = new ArrayList<>();

  @Inject
  public DefaultRemoteConfigRegistry(
      RemoteConfigScanner remoteConfigScanner) {
    remoteConfigScanner.accept(remoteConfigs::add);
  }

  @Override
  public Collection<RemoteConfig> getRemoteConfigs() {
    remoteConfigs.sort(Comparator.comparingInt(RemoteConfig::order));
    return ImmutableList.copyOf(remoteConfigs);
  }
}
