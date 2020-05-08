package com.github.zhengframework.shiro.web;

/*-
 * #%L
 * zheng-shiro-web
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

import java.util.Objects;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import org.apache.shiro.web.env.DefaultWebEnvironment;
import org.apache.shiro.web.env.EnvironmentLoader;
import org.apache.shiro.web.env.WebEnvironment;
import org.apache.shiro.web.filter.mgt.FilterChainResolver;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.servlet.AbstractShiroFilter;

public class GuiceShiroFilter extends AbstractShiroFilter {

  private WebEnvironment env;

  @Inject
  GuiceShiroFilter(WebEnvironment env) {
    this.env = env;
  }

  @Override
  public void init() {
    DefaultWebEnvironment webEnvironment = (DefaultWebEnvironment) env;
    ServletContext servletContext =
        Objects.requireNonNull(getServletContext(), "servletContext is null");
    webEnvironment.setServletContext(servletContext);
    servletContext.setAttribute(EnvironmentLoader.ENVIRONMENT_ATTRIBUTE_KEY, webEnvironment);
    WebSecurityManager webSecurityManager =
        Objects.requireNonNull(env.getWebSecurityManager(), "WebSecurityManager is null");
    this.setSecurityManager(webSecurityManager);
    this.setStaticSecurityManagerEnabled(true);
    FilterChainResolver resolver =
        Objects.requireNonNull(env.getFilterChainResolver(), "FilterChainResolver is null");
    setFilterChainResolver(resolver);
  }
}
