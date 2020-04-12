package com.github.zhengframework.bootstrap;

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
  public final ZhengApplicationBuilder excludeModule(
      Class<? extends Module>... moduleList) {
    Preconditions.checkNotNull(moduleList);
    this.excludeModuleList.addAll(Arrays.asList(moduleList));
    return this;
  }

  public ZhengApplicationBuilder excludeModule(
      Set<Class<? extends Module>> moduleList) {
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
    return new ZhengApplication(configuration, arguments, moduleList, autoLoadModule,
        excludeModuleList, excludeModuleProviderList);
  }


}
