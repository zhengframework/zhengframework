package com.dadazhishi.zheng.configuration.source;

import com.dadazhishi.zheng.configuration.environment.Environment;
import com.dadazhishi.zheng.configuration.ex.MissingEnvironmentException;
import com.dadazhishi.zheng.configuration.io.AbsoluteNameLocationStrategy;
import com.dadazhishi.zheng.configuration.io.BasePathLocationStrategy;
import com.dadazhishi.zheng.configuration.io.ClasspathLocationStrategy;
import com.dadazhishi.zheng.configuration.io.CombinedLocationStrategy;
import com.dadazhishi.zheng.configuration.io.DefaultFileSystem;
import com.dadazhishi.zheng.configuration.io.FileLocationStrategy;
import com.dadazhishi.zheng.configuration.io.FileLocator;
import com.dadazhishi.zheng.configuration.io.FileSystem;
import com.dadazhishi.zheng.configuration.io.FileSystemLocationStrategy;
import com.dadazhishi.zheng.configuration.io.HomeDirectoryLocationStrategy;
import com.dadazhishi.zheng.configuration.io.ProvidedURLLocationStrategy;
import com.dadazhishi.zheng.configuration.parser.FileConfigurationParser;
import com.dadazhishi.zheng.configuration.parser.FileConfigurationParserSelector;
import com.dadazhishi.zheng.configuration.parser.JsonConfigurationParser;
import com.dadazhishi.zheng.configuration.parser.PropertiesConfigurationParser;
import com.dadazhishi.zheng.configuration.parser.YamlConfigurationParser;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public class FileConfigurationSource implements ConfigurationSource {

  public static final CombinedLocationStrategy FILE_LOCATION_STRATEGY = new CombinedLocationStrategy(
      Arrays.asList(
          new ProvidedURLLocationStrategy(),
          new AbsoluteNameLocationStrategy(),
          new BasePathLocationStrategy(),
          new HomeDirectoryLocationStrategy(),
          new FileSystemLocationStrategy(),
          new ClasspathLocationStrategy()
      ));
  public static final FileConfigurationParserSelector FILE_CONFIGURATION_PARSER = new FileConfigurationParserSelector(
      Arrays.asList(
          new PropertiesConfigurationParser()
          , new YamlConfigurationParser()
          , new JsonConfigurationParser()
      ));
  private static final String ENV_PATTERN = "${env}";
  private final FileSystem fileSystem;
  private final FileLocationStrategy fileLocationStrategy;
  private final FileLocator fileLocator;
  private final FileConfigurationParser fileConfigurationParser;

  public FileConfigurationSource() {
    this("application.properties");
  }

  public FileConfigurationSource(
      String fileName) {
    this(FileLocator.builder().fileName(fileName).build());
  }

  public FileConfigurationSource(FileLocator fileLocator) {
    this(FILE_LOCATION_STRATEGY, fileLocator);
  }

  public FileConfigurationSource(
      FileLocationStrategy fileLocationStrategy,
      FileLocator fileLocator) {
    this(new DefaultFileSystem(), fileLocationStrategy, fileLocator);
  }

  public FileConfigurationSource(FileSystem fileSystem,
      FileLocationStrategy fileLocationStrategy,
      FileLocator fileLocator) {
    this(fileSystem, fileLocationStrategy, fileLocator, FILE_CONFIGURATION_PARSER);
  }

  public FileConfigurationSource(FileSystem fileSystem,
      FileLocationStrategy fileLocationStrategy,
      FileLocator fileLocator,
      FileConfigurationParser fileConfigurationParser) {
    Preconditions.checkArgument(fileLocationStrategy != null, "fileLocationStrategy is null");
    Preconditions.checkArgument(fileSystem != null, "fileSystem is null");
    Preconditions.checkArgument(fileLocator != null, "fileLocator is null");
    Preconditions.checkArgument(fileConfigurationParser != null, "fileConfigurationParser is null");
    this.fileSystem = fileSystem;
    this.fileLocationStrategy = fileLocationStrategy;
    this.fileLocator = fileLocator;
    this.fileConfigurationParser = fileConfigurationParser;
  }

  @Override
  public void init() {

  }

  @Override
  public Map<String, String> getConfiguration(Environment environment) {
    String env = StringUtils.trimToEmpty(environment.getName());
    FileLocator copy = fileLocator.copy();
    if (StringUtils.isNotEmpty(copy.getSourceURL())) {
      String sourceURL = StringUtils.trimToEmpty(copy.getSourceURL());
      if (StringUtils.isEmpty(env) && sourceURL.contains(ENV_PATTERN)) {
        throw new MissingEnvironmentException("sourceURL need environment: " + sourceURL);
      }
      copy.setSourceURL(sourceURL.replace(ENV_PATTERN, env));
    }
    if (StringUtils.isNotEmpty(copy.getBasePath())) {
      String basePath = StringUtils.trimToEmpty(copy.getBasePath());
      if (StringUtils.isEmpty(env) && basePath.contains(ENV_PATTERN)) {
        throw new MissingEnvironmentException("basePath need environment: " + basePath);
      }
      copy.setBasePath(basePath.replace(ENV_PATTERN, env));
    }
    if (StringUtils.isNotEmpty(copy.getFileName())) {
      String fileName = StringUtils.trimToEmpty(copy.getFileName());
      if (StringUtils.isEmpty(env) && fileName.contains(ENV_PATTERN)) {
        throw new MissingEnvironmentException("fileName need environment: " + fileName);
      }
      copy.setFileName(fileName.replace(ENV_PATTERN, env));
    }
    URL url = fileLocationStrategy.locate(fileSystem, copy);
    try (InputStream inputStream = url.openStream()) {
      return Collections
          .unmodifiableMap(fileConfigurationParser.parse(url.toString(), inputStream));
    } catch (IOException e) {
      throw new IllegalStateException("Unable to load configuration from file: " + url, e);
    }
  }
}
