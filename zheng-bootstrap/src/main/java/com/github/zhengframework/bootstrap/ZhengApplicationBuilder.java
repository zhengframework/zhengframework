package com.github.zhengframework.bootstrap;

/*-
 * #%L
 * zheng-bootstrap
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
import com.github.zhengframework.core.ModuleProvider;
import com.google.common.base.Preconditions;
import com.google.inject.Module;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public final class ZhengApplicationBuilder {

  private Configuration configuration;
  private Arguments arguments = new Arguments(new String[0]);
  private Set<Module> moduleList = new LinkedHashSet<>();
  private boolean autoLoadModule = true;
  private Set<Class<? extends Module>> excludeModuleList = new LinkedHashSet<>();
  private Set<Class<? extends ModuleProvider>> excludeModuleProviderList = new LinkedHashSet<>();

  private ZhengApplicationBuilder() {
  }

  public static ZhengApplicationBuilder create() {
    return new ZhengApplicationBuilder();
  }

  public ZhengApplicationBuilder withConfiguration(Configuration configuration) {
    Preconditions.checkNotNull(configuration);
    this.configuration = configuration;
    return this;
  }

  public ZhengApplicationBuilder withArguments(String[] arguments) {
    Preconditions.checkNotNull(arguments);
    this.arguments = new Arguments(arguments);
    return this;
  }

  public ZhengApplicationBuilder addModule(Module... moduleList) {
    Preconditions.checkNotNull(moduleList);
    this.moduleList.addAll(Arrays.asList(moduleList));
    return this;
  }

  public ZhengApplicationBuilder addModule(Collection<Module> moduleList) {
    Preconditions.checkNotNull(moduleList);
    this.moduleList.addAll(moduleList);
    return this;
  }

  public ZhengApplicationBuilder enableAutoLoadModule() {
    this.autoLoadModule = true;
    return this;
  }

  public ZhengApplicationBuilder disableAutoLoadModule() {
    this.autoLoadModule = false;
    return this;
  }

  public ZhengApplicationBuilder withAutoLoadModule(boolean autoLoadModule) {
    this.autoLoadModule = autoLoadModule;
    return this;
  }

  @SafeVarargs
  public final ZhengApplicationBuilder excludeModule(Class<? extends Module>... moduleList) {
    Preconditions.checkNotNull(moduleList);
    this.excludeModuleList.addAll(Arrays.asList(moduleList));
    return this;
  }

  public ZhengApplicationBuilder excludeModule(Set<Class<? extends Module>> moduleList) {
    Preconditions.checkNotNull(moduleList);
    this.excludeModuleList.addAll(moduleList);
    return this;
  }

  @SafeVarargs
  public final ZhengApplicationBuilder excludeModuleProvider(
      Class<? extends ModuleProvider>... moduleProviderList) {
    Preconditions.checkNotNull(moduleProviderList);
    this.excludeModuleProviderList.addAll(Arrays.asList(moduleProviderList));
    return this;
  }

  public ZhengApplicationBuilder excludeModuleProvider(
      Set<Class<? extends ModuleProvider>> moduleProviderList) {
    Preconditions.checkNotNull(moduleProviderList);
    this.excludeModuleProviderList.addAll(moduleProviderList);
    return this;
  }

  public ZhengApplication build() {
    return new ZhengApplication(
        configuration,
        arguments,
        moduleList,
        autoLoadModule,
        excludeModuleList,
        excludeModuleProviderList);
  }
}
