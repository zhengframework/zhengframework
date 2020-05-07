package com.github.zhengframework.metrics.servlet;

/*-
 * #%L
 * zheng-metrics-servlet
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

import com.codahale.metrics.servlets.AdminServlet;
import com.github.zhengframework.configuration.Configuration;
import com.github.zhengframework.configuration.ConfigurationAwareServletModule;
import com.github.zhengframework.configuration.ConfigurationBeanMapper;
import com.github.zhengframework.metrics.MetricsConfig;
import com.github.zhengframework.web.PathUtils;
import com.github.zhengframework.web.WebConfig;
import java.util.Map;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MetricsServletModule extends ConfigurationAwareServletModule {

  @Override
  protected void configureServlets() {
    Configuration configuration = getConfiguration();
    Map<String, MetricsConfig> metricsConfigMap =
        ConfigurationBeanMapper.resolve(configuration, MetricsConfig.class);
    MetricsConfig metricsConfig = metricsConfigMap.getOrDefault("", new MetricsConfig());

    Map<String, MetricsServletConfig> metricsServletConfigMap =
        ConfigurationBeanMapper.resolve(configuration, MetricsServletConfig.class);
    MetricsServletConfig metricsServletConfig =
        metricsServletConfigMap.getOrDefault("", new MetricsServletConfig());
    if (metricsConfig.isEnable()) {
      if (metricsServletConfig.isEnable()) {
        Map<String, WebConfig> webConfigMap =
            ConfigurationBeanMapper.resolve(configuration, WebConfig.class);
        WebConfig webConfig = webConfigMap.getOrDefault("", new WebConfig());

        String path = PathUtils.fixPath(metricsServletConfig.getPath());
        serve(path + "/*").with(AdminServlet.class);
        bind(AdminServlet.class).in(Singleton.class);
        log.info(
            "Metrics Admin Page: {}",
            "http://localhost:"
                + webConfig.getPort()
                + PathUtils.fixPath(webConfig.getContextPath(), path)
                + "/");
        bind(MetricsServletContextListener.class).in(Singleton.class);
        bind(HealthCheckServletContextListener.class).in(Singleton.class);
      } else {
        log.warn("MetricsServletModule is disable");
      }
    } else {
      log.warn("MetricsModule is disable");
    }
  }
}
