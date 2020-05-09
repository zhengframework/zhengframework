package com.github.zhengframework.remoteconfig;

import com.github.zhengframework.guice.ClassScanner;
import com.google.inject.Injector;
import javax.inject.Inject;

public class RemoteConfigScanner extends ClassScanner<RemoteConfig> {

  @Inject
  public RemoteConfigScanner(Injector injector) {
    super(injector, RemoteConfig.class);
  }
}
