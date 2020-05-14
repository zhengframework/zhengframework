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
import com.github.zhengframework.configuration.ConfigurationAware;
import com.github.zhengframework.configuration.ConfigurationBeanMapper;
import com.github.zhengframework.configuration.ConfigurationBuilder;
import com.github.zhengframework.configuration.io.FileLocator;
import com.github.zhengframework.configuration.source.ConfigurationSource;
import com.github.zhengframework.configuration.source.EnvironmentVariablesConfigurationSource;
import com.github.zhengframework.configuration.source.FileConfigurationSource;
import com.github.zhengframework.configuration.source.MergeConfigurationSource;
import com.github.zhengframework.configuration.source.SystemPropertiesConfigurationSource;
import com.github.zhengframework.core.ModuleProvider;
import com.github.zhengframework.service.ServiceManager;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.stream.Collectors;
import joptsimple.ArgumentAcceptingOptionSpec;
import joptsimple.OptionSet;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class ZhengApplication {

  public static final String APPLICATION_FILE = "zheng.application.file";

  private Configuration configuration;

  private Arguments arguments;

  private Set<Module> moduleList;

  private boolean autoLoadModule;

  private Set<Class<? extends Module>> excludeModuleList;

  private Set<Class<? extends ModuleProvider>> excludeModuleProviderList;

  private Injector injector;

  public ZhengApplication(
      Configuration configuration,
      Arguments arguments,
      Set<Module> moduleList,
      boolean autoLoadModule,
      Set<Class<? extends Module>> excludeModuleList,
      Set<Class<? extends ModuleProvider>> excludeModuleProviderList) {
    this.configuration = configuration;
    this.arguments = arguments;
    this.moduleList = moduleList;
    this.autoLoadModule = autoLoadModule;
    this.excludeModuleList = excludeModuleList;
    this.excludeModuleProviderList = excludeModuleProviderList;
    build();
  }

  private void build() {
    if (configuration == null) {
      configuration = buildConfiguration(arguments);
    }
    Set<Module> moduleListCopy = new LinkedHashSet<>(moduleList);
    if (autoLoadModule) {
      for (ModuleProvider moduleProvider : ServiceLoader.load(ModuleProvider.class)) {
        if (!excludeModuleProviderList.contains(moduleProvider.getClass())) {
          log.info("find ModuleProvider={}", moduleProvider.getClass());
          moduleListCopy.add(moduleProvider.getModule());
        }
      }
    }
    Set<Module> modules =
        moduleListCopy.stream()
            .filter(module -> !excludeModuleList.contains(module.getClass()))
            .collect(Collectors.toSet());
    for (Module module : modules) {
      if (module instanceof ConfigurationAware) {
        ConfigurationAware configurationAware = (ConfigurationAware) module;
        configurationAware.initConfiguration(configuration);
      }
    }

    GuiceConfig guiceConfig =
        ConfigurationBeanMapper.resolve(configuration, GuiceConfig.ZHENG_GUICE, GuiceConfig.class);
    injector = Guice.createInjector(guiceConfig.getStage(), modules);
  }

  public Injector getInjector() {
    return injector;
  }

  public void start() throws Exception {
    injector.getInstance(ServiceManager.class).start();
  }

  public void stop() {
    injector.getInstance(ServiceManager.class).stop();
  }

  private Configuration buildConfiguration(Arguments arguments) {
    ArgumentAcceptingOptionSpec<String> configOpt =
        arguments.getOptionParser().accepts("config").withRequiredArg().ofType(String.class);
    OptionSet optionSet = arguments.parse();

    Optional<String> config = optionSet.valueOfOptional(configOpt);
    String argsConfigFile = null;
    if (config.isPresent()) {
      argsConfigFile = config.get();
    }

    Path path = null;
    if (argsConfigFile != null) {
      path = Paths.get(argsConfigFile);
      Preconditions.checkState(
          Files.exists(path) && Files.isReadable(path),
          argsConfigFile + " not exists or not readable");
    }
    String env = System.getenv(APPLICATION_FILE);
    if (path == null && !Strings.isNullOrEmpty(env)) {
      path = Paths.get(env);
      Preconditions.checkState(
          Files.exists(path) && Files.isReadable(path), env + " not exists or not readable");
    }
    String property = System.getProperty(APPLICATION_FILE);
    if (path == null && !Strings.isNullOrEmpty(property)) {
      path = Paths.get(property);
      Preconditions.checkState(
          Files.exists(path) && Files.isReadable(path), property + " not exists or not readable");
    }

    List<ConfigurationSource> sources = new ArrayList<>();
    sources.add(new SystemPropertiesConfigurationSource());
    sources.add(new EnvironmentVariablesConfigurationSource());

    if (path != null) {
      sources.add(
          0,
          new FileConfigurationSource(
              FileLocator.builder().sourceURL(path.toAbsolutePath().toString()).build()));
    } else {
      sources.add(new FileConfigurationSource("application.properties"));
    }
    MergeConfigurationSource configurationSource = new MergeConfigurationSource(sources);
    return new ConfigurationBuilder().withConfigurationSource(configurationSource).build();
  }
}
