package com.github.zhengframework.webjars;

/*-
 * #%L
 * zheng-webjars
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
import com.github.zhengframework.web.PathUtils;
import java.util.Collections;
import java.util.Map;
import javax.inject.Singleton;

public class WebjarsModule extends ConfigurationAwareServletModule {

  @Override
  protected void configureServlets() {
    Map<String, WebjarsConfig> configMap =
        ConfigurationBeanMapper.resolve(getConfiguration(), WebjarsConfig.class);
    WebjarsConfig webjarsConfig = configMap.getOrDefault("", new WebjarsConfig());
    bind(WebjarsConfig.class).toInstance(webjarsConfig);
    bind(WebjarsServlet.class).in(Singleton.class);
    String path = webjarsConfig.getBasePath();
    path = PathUtils.fixPath(path);
    Map<String, String> initParams =
        Collections.singletonMap("disableCache", "" + webjarsConfig.isDisableCache());
    if (path == null) {
      serve("/webjars/*").with(WebjarsServlet.class, initParams);
    } else {
      serve(path + "/*").with(WebjarsServlet.class, initParams);
    }
  }
}
