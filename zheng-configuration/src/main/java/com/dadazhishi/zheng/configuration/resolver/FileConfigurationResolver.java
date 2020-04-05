package com.dadazhishi.zheng.configuration.resolver;

import com.dadazhishi.zheng.configuration.ex.ConfigurationException;
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
import com.dadazhishi.zheng.configuration.parser.ConfigurationParser;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileConfigurationResolver extends ReloadableConfigurationResolver {

  public static final CombinedLocationStrategy FILE_LOCATION_STRATEGY = new CombinedLocationStrategy(
      Arrays.asList(
          new ProvidedURLLocationStrategy(),
          new AbsoluteNameLocationStrategy(),
          new BasePathLocationStrategy(),
          new HomeDirectoryLocationStrategy(),
          new FileSystemLocationStrategy(),
          new ClasspathLocationStrategy()
      ));

  private final FileSystem fileSystem;
  private final FileLocationStrategy fileLocationStrategy;
  private final FileLocator fileLocator;
  private final ConfigurationParser<InputStream> configurationParser;
  private final boolean failOnError;

  public FileConfigurationResolver(FileSystem fileSystem,
      FileLocationStrategy fileLocationStrategy,
      FileLocator fileLocator,
      ConfigurationParser<InputStream> configurationParser,
      boolean failOnError) {
    this.fileSystem = fileSystem;
    this.fileLocationStrategy = fileLocationStrategy;
    this.fileLocator = fileLocator;
    this.configurationParser = configurationParser;
    this.failOnError = failOnError;
    Preconditions.checkArgument(fileLocationStrategy != null, "fileLocationStrategy is null");
    Preconditions.checkArgument(fileSystem != null, "fileSystem is null");
    Preconditions.checkArgument(fileLocator != null, "fileLocator is null");
    Preconditions.checkArgument(configurationParser != null, "configurationParser is null");
    reload();
  }

  public FileConfigurationResolver(
      FileLocationStrategy fileLocationStrategy,
      FileLocator fileLocator, ConfigurationParser<InputStream> configurationParser,
      boolean failOnError) {
    this(new DefaultFileSystem(), fileLocationStrategy, fileLocator, configurationParser,
        failOnError);
  }

  public FileConfigurationResolver(
      FileLocator fileLocator, ConfigurationParser<InputStream> configurationParser,
      boolean failOnError) {
    this(new DefaultFileSystem(), FILE_LOCATION_STRATEGY, fileLocator, configurationParser,
        failOnError);
  }

  public FileConfigurationResolver(
      FileLocator fileLocator,
      ConfigurationParser<InputStream> configurationParser) {
    this(fileLocator, configurationParser, true);
  }

  @Override
  public void reload() {
    URL url = fileLocationStrategy.locate(fileSystem, fileLocator);
    if (url != null) {
      try (InputStream inputStream = url.openStream()) {
        update(configurationParser.parse(inputStream));
      } catch (IOException e) {
        if (failOnError) {
          throw new ConfigurationException(
              "read configuration from file fail, url=" + url, e);
        } else {
          log.warn("read configuration from file fail, url={}", url);
        }
      }
    }
  }
}
