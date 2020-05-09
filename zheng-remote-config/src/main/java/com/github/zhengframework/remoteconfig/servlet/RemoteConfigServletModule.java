package com.github.zhengframework.remoteconfig.servlet;

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

import com.github.zhengframework.configuration.ConfigurationAwareServletModule;
import com.github.zhengframework.configuration.ConfigurationBeanMapper;
import com.github.zhengframework.remoteconfig.RemoteConfigServerConfig;
import com.github.zhengframework.web.PathUtils;
import java.util.Map;
import javax.inject.Singleton;

public class RemoteConfigServletModule extends ConfigurationAwareServletModule {

  @Override
  protected void configureServlets() {
    Map<String, RemoteConfigServerConfig> configMap =
        ConfigurationBeanMapper.resolve(getConfiguration(), RemoteConfigServerConfig.class);
    RemoteConfigServerConfig remoteConfigServerConfig =
        configMap.getOrDefault("", new RemoteConfigServerConfig());
    if (remoteConfigServerConfig.isEnable()) {
      Map<String, RemoteConfigServerServletConfig> configMap2 =
          ConfigurationBeanMapper.resolve(
              getConfiguration(), RemoteConfigServerServletConfig.class);
      RemoteConfigServerServletConfig remoteConfigServerServletConfig =
          configMap2.getOrDefault("", new RemoteConfigServerServletConfig());
      bind(RemoteConfigServerServletConfig.class).toInstance(remoteConfigServerServletConfig);
      if (remoteConfigServerServletConfig.isEnable()) {
        bind(RemoteConfigServerServlet.class).in(Singleton.class);
        String path = remoteConfigServerServletConfig.getBasePath();
        path = PathUtils.fixPath(path);
        serve(path + "/*").with(RemoteConfigServerServlet.class);
      }
    }
  }
}
