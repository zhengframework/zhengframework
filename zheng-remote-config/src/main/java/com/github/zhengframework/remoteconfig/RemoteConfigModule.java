package com.github.zhengframework.remoteconfig;

import com.github.zhengframework.configuration.ConfigurationAwareModule;
import com.github.zhengframework.configuration.ConfigurationBeanMapper;
import com.google.inject.multibindings.OptionalBinder;
import java.util.Map;
import javax.inject.Singleton;

public class RemoteConfigModule extends ConfigurationAwareModule {

  @Override
  protected void configure() {
    Map<String, RemoteConfigServerConfig> configMap =
        ConfigurationBeanMapper.resolve(getConfiguration(), RemoteConfigServerConfig.class);
    RemoteConfigServerConfig remoteConfigServerConfig = configMap
        .getOrDefault("", new RemoteConfigServerConfig());
    bind(RemoteConfigServerConfig.class).toInstance(remoteConfigServerConfig);

    if(remoteConfigServerConfig.isEnable()){
      OptionalBinder.newOptionalBinder(binder(), RemoteConfigRegistry.class)
          .setDefault().to(DefaultRemoteConfigRegistry.class).in(Singleton.class);

      OptionalBinder.newOptionalBinder(binder(), RemoteConfigServer.class)
          .setDefault().to(DefaultRemoteConfigServer.class).in(Singleton.class);
      OptionalBinder.newOptionalBinder(binder(), RemoteConfigExceptionMapper.class)
          .setDefault().to(DefaultRemoteConfigExceptionMapper.class).in(Singleton.class);
    }

  }

}
