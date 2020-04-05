package com.dadazhishi.zheng.service;

import com.dadazhishi.zheng.configuration.Configuration;
import com.dadazhishi.zheng.configuration.ConfigurationAware;
import com.dadazhishi.zheng.configuration.ConfigurationBuilder;
import com.dadazhishi.zheng.configuration.ex.ConfigurationException;
import com.dadazhishi.zheng.configuration.io.FileLocator;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class ZhengApplication {

  private Injector injector;
  private Options options = new Options();

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
    Option option = Option.builder().longOpt("config").hasArg(true)
        .desc("app configuration file path").build();
    options.addOption(option);

    CommandLineParser parser = new DefaultParser();
    String argsConfigFile = null;
    try {
      CommandLine commandLine = parser.parse(options, args);
      if (commandLine.hasOption("config")) {
        argsConfigFile = commandLine.getOptionValue("config");
      }
    } catch (ParseException e) {
      throw new RuntimeException("parse arguments fail", e);
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

    ConfigurationBuilder configurationBuilder = ConfigurationBuilder.create();
    ConfigurationBuilder builder = configurationBuilder
        .withSystemProperties()
        .withEnvironmentVariables();
    if (path != null) {
      try {
        builder.withProperties(FileLocator.builder().sourceURL(path.toUri().toURL()).build());
      } catch (MalformedURLException e) {
        throw new ConfigurationException(e);
      }
    } else {
      builder.withProperties("application.properties");
    }
    return builder.build();
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
