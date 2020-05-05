package com.github.zhengframework.swagger;

/*-
 * #%L
 * zheng-swagger
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


import com.github.zhengframework.configuration.Configuration;
import com.github.zhengframework.configuration.ConfigurationAwareServletModule;
import com.github.zhengframework.configuration.ConfigurationBeanMapper;
import com.github.zhengframework.rest.RestConfig;
import com.github.zhengframework.web.PathUtils;
import com.github.zhengframework.web.WebConfig;
import io.swagger.v3.jaxrs2.integration.resources.AcceptHeaderOpenApiResource;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import java.util.Map;
import javax.inject.Singleton;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EqualsAndHashCode(callSuper = false, of = {})
public class SwaggerModule extends ConfigurationAwareServletModule {

  @Override
  protected void configureServlets() {
    Configuration configuration = getConfiguration();
    Map<String, SwaggerConfig> configMap = ConfigurationBeanMapper
        .resolve(configuration, SwaggerConfig.class);
    SwaggerConfig swaggerConfig = configMap.getOrDefault("", new SwaggerConfig());

    bind(SwaggerConfig.class).toInstance(swaggerConfig);

    Map<String, WebConfig> webConfigMap = ConfigurationBeanMapper
        .resolve(configuration, WebConfig.class);
    WebConfig webConfig = webConfigMap.getOrDefault("", new WebConfig());

    Map<String, RestConfig> restConfigMap = ConfigurationBeanMapper
        .resolve(configuration, RestConfig.class);
    RestConfig restConfig = restConfigMap.getOrDefault("", new RestConfig());

    bind(OpenApiResource.class);
    bind(AcceptHeaderOpenApiResource.class);
    String contextPath = PathUtils
        .fixPath(webConfig.getContextPath(), restConfig.getPath());
    String json = "http://localhost:" + webConfig.getPort() + contextPath + "/openapi.json";
    String yaml = "http://localhost:" + webConfig.getPort() + contextPath + "/openapi.yaml";
    log.info("open api json format: {}", json);
    log.info("open api yaml format: {}", yaml);
    swaggerConfig.setApiUrl(json);

    if (swaggerConfig.isEnableUI()) {
      bind(SwaggerUIServlet.class).in(Singleton.class);
      String path = PathUtils.fixPath(swaggerConfig.getUiPath());
      serve(path + "/*").with(SwaggerUIServlet.class);
      log.info("open api browser: {}", "http://localhost:" + webConfig.getPort() + PathUtils
          .fixPath(webConfig.getContextPath(), path) + "/");
    } else {
      log.info("open api browser is disable");
    }
  }

}
