package com.github.zhengframework.bootstrap;

import com.github.zhengframework.configuration.ConfigurationBuilder;
import com.github.zhengframework.configuration.io.FileLocator;
import com.github.zhengframework.configuration.source.ConfigurationSource;
import com.github.zhengframework.configuration.source.EnvironmentVariablesConfigurationSource;
import com.github.zhengframework.configuration.source.FallbackConfigurationSource;
import com.github.zhengframework.configuration.source.FileConfigurationSource;
import com.github.zhengframework.configuration.source.SystemPropertiesConfigurationSource;
import com.github.zhengframework.core.Configuration;
import com.github.zhengframework.core.ConfigurationAware;
import com.github.zhengframework.core.ServiceManager;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import joptsimple.ArgumentAcceptingOptionSpec;
import joptsimple.OptionSet;

public class Application {

  private Injector injector;

  public Application(Configuration configuration, String[] args, Module... modules) {
    if (configuration == null) {
      configuration = buildConfiguration(args);
    }
    for (Module module : modules) {
      if (module instanceof ConfigurationAware) {
        ConfigurationAware configurationAware = (ConfigurationAware) module;
        configurationAware.initConfiguration(configuration);
      }
    }
    injector = Guice.createInjector(Stage.PRODUCTION, modules);
  }

  public static Application create(Configuration configuration, String[] args,
      Module... modules) {
    return new Application(configuration, args, modules);
  }

  public static Application create(Configuration configuration, Module... modules) {
    return new Application(configuration, new String[0], modules);
  }

  public static Application create(Module... modules) {
    return new Application(null, new String[0], modules);
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

  public void start() throws Exception {
    injector.getInstance(ServiceManager.class).start();
  }

  public void stop() {
    injector.getInstance(ServiceManager.class).stop();
  }

}
