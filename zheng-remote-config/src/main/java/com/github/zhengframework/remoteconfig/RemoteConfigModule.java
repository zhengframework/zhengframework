package com.github.zhengframework.remoteconfig;

/*-
 * #%L
 * zheng-remote-config
 * %%
 * Copyright (C) 2020 Zheng MingHai
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.github.zhengframework.configuration.ConfigurationAwareModule;
import com.github.zhengframework.configuration.ConfigurationBeanMapper;
import com.google.inject.multibindings.OptionalBinder;
import javax.inject.Singleton;

public class RemoteConfigModule extends ConfigurationAwareModule {

  @Override
  protected void configure() {
    RemoteConfigServerConfig remoteConfigServerConfig =
        ConfigurationBeanMapper.resolve(
            getConfiguration(),
            RemoteConfigServerConfig.ZHENG_REMOTE_CONFIG,
            RemoteConfigServerConfig.class);

    bind(RemoteConfigServerConfig.class).toInstance(remoteConfigServerConfig);

    if (remoteConfigServerConfig.isEnable()) {
      OptionalBinder.newOptionalBinder(binder(), RemoteConfigRegistry.class)
          .setDefault()
          .to(DefaultRemoteConfigRegistry.class)
          .in(Singleton.class);

      OptionalBinder.newOptionalBinder(binder(), RemoteConfigServer.class)
          .setDefault()
          .to(DefaultRemoteConfigServer.class)
          .in(Singleton.class);
      OptionalBinder.newOptionalBinder(binder(), RemoteConfigExceptionMapper.class)
          .setDefault()
          .to(DefaultRemoteConfigExceptionMapper.class)
          .in(Singleton.class);
    }
  }
}
