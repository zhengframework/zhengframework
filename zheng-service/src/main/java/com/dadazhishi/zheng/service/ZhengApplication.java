package com.dadazhishi.zheng.service;

import com.dadazhishi.zheng.configuration.Configuration;
import com.dadazhishi.zheng.configuration.ConfigurationAware;
import com.dadazhishi.zheng.configuration.ConfigurationBuilder;
import com.dadazhishi.zheng.configuration.io.FileLocator;
import com.dadazhishi.zheng.configuration.source.ConfigurationSource;
import com.dadazhishi.zheng.configuration.source.EnvironmentVariablesConfigurationSource;
import com.dadazhishi.zheng.configuration.source.FallbackConfigurationSource;
import com.dadazhishi.zheng.configuration.source.FileConfigurationSource;
import com.dadazhishi.zheng.configuration.source.SystemPropertiesConfigurationSource;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import joptsimple.ArgumentAcceptingOptionSpec;
import joptsimple.OptionSet;

public class ZhengApplication {

  private Injector injector;

  public ZhengApplication(Configuration configuration, String[] args, Module... modules) {
    if (configuration == null) {
      configuration = buildConfiguration(args);
    }
    for (Module module : modules) {
      if (module instanceof ConfigurationAware) {
        ConfigurationAware configurationAware = (ConfigurationAware) module;
        configurationAware.initConfiguration(configuration);
      }
    }
    injector = Guice.createInjector(modules);
  }

  public static ZhengApplication create(Configuration configuration, String[] args,
      Module... modules) {
    return new ZhengApplication(configuration, args, modules);
  }

  public static ZhengApplication create(Configuration configuration, Module... modules) {
    return new ZhengApplication(configuration, new String[0], modules);
  }

  public static ZhengApplication create(Module... modules) {
    return new ZhengApplication(null, new String[0], modules);
  }

  private Configuration buildConfiguration(String[] args) {
    Arguments arguments = new Arguments(args);
    ArgumentAcceptingOptionSpec<String> configOpt = arguments.getOptionParser().accepts("config")
        .withRequiredArg().ofType(String.class);
    OptionSet optionSet = arguments.parse();

    Optional<String> config = optionSet.valueOfOptional(configOpt);
    String argsConfigFile = null;
    if (config.isPresent()) {
      argsConfigFile = config.get();
    }

    Path path = null;
    if (argsConfigFile != null) {
      path = Paths.get(argsConfigFile);
      Preconditions.checkState(Files.exists(path) && Files.isReadable(path),
          argsConfigFile + " not exists or not readable");
    }
    String env = System.getenv("zheng.application.file");
    if (path == null && !Strings.isNullOrEmpty(env)) {
      path = Paths.get(env);
      Preconditions.checkState(Files.exists(path) && Files.isReadable(path),
          env + " not exists or not readable");
    }
    String property = System.getProperty("zheng.application.file");
    if (path == null && !Strings.isNullOrEmpty(property)) {
      path = Paths.get(property);
      Preconditions.checkState(Files.exists(path) && Files.isReadable(path),
          property + " not exists or not readable");
    }

    List<ConfigurationSource> sources = new ArrayList<>();
    sources.add(new SystemPropertiesConfigurationSource());
    sources.add(new EnvironmentVariablesConfigurationSource());

    if (path != null) {
      sources.add(0, new FileConfigurationSource(
          FileLocator.builder().sourceURL(path.toAbsolutePath().toString()).build()));
    } else {
      sources.add(new FileConfigurationSource("application.properties"));
    }
    FallbackConfigurationSource configurationSource = new FallbackConfigurationSource(
        sources);
    return new ConfigurationBuilder()
        .withConfigurationSource(configurationSource)
        .build();
  }

  public Injector getInjector() {
    return injector;
  }

  public void start() {
    injector.getInstance(Run.class).start();
  }

  public void stop() {
    injector.getInstance(Run.class).stop();
  }

}
