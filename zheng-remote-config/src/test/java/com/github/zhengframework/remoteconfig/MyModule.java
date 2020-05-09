package com.github.zhengframework.remoteconfig;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.OptionalBinder;
import javax.inject.Singleton;

public class MyModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(EchoRemoteConfig.class);
    bind(ThrowableRemoteConfig.class);
    bind(MultiKeyRemoteConfig.class);
    bind(CustomThrowableRemoteConfig.class);
    OptionalBinder.newOptionalBinder(binder(), RemoteConfigExceptionMapper.class)
        .setBinding().to(CustomRemoteConfigExceptionMapper.class).in(Singleton.class);
  }
}
