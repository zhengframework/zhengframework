package com.dadazhishi.zheng.configuration.resolver;

import static com.dadazhishi.zheng.configuration.resolver.FileConfigurationResolver.FILE_LOCATION_STRATEGY;

import com.dadazhishi.zheng.configuration.ex.ConfigurationException;
import com.dadazhishi.zheng.configuration.io.DefaultFileSystem;
import com.dadazhishi.zheng.configuration.io.FileLocationStrategy;
import com.dadazhishi.zheng.configuration.io.FileLocator;
import com.dadazhishi.zheng.configuration.io.FileSystem;
import com.dadazhishi.zheng.configuration.parser.AutoConfigurationParserSelector;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AutoFileConfigurationResolver extends ReloadableConfigurationResolver {

  private final FileSystem fileSystem;
  private final FileLocationStrategy fileLocationStrategy;
  private final FileLocator fileLocator;
  private final AutoConfigurationParserSelector configurationParserSelector;
  private final boolean failOnError;

  public AutoFileConfigurationResolver(FileSystem fileSystem,
      FileLocationStrategy fileLocationStrategy,
      FileLocator fileLocator,
      boolean failOnError,
      Map<String, String> properties) {
    this.fileSystem = fileSystem;
    this.fileLocationStrategy = fileLocationStrategy;
    this.fileLocator = fileLocator;
    this.configurationParserSelector = new AutoConfigurationParserSelector(properties);
    this.failOnError = failOnError;
    Preconditions.checkArgument(fileLocationStrategy != null, "fileLocationStrategy is null");
    Preconditions.checkArgument(fileSystem != null, "fileSystem is null");
    Preconditions.checkArgument(fileLocator != null, "fileLocator is null");
    reload();
  }

  public AutoFileConfigurationResolver(
      FileLocator fileLocator, boolean failOnError, Map<String, String> properties) {
    this(new DefaultFileSystem(), FILE_LOCATION_STRATEGY, fileLocator, failOnError, properties);
  }

  public AutoFileConfigurationResolver(
      FileLocator fileLocator, boolean failOnError) {
    this(new DefaultFileSystem(), FILE_LOCATION_STRATEGY, fileLocator, failOnError,
        Collections.emptyMap());
  }

  public AutoFileConfigurationResolver(
      FileLocator fileLocator, Map<String, String> properties) {
    this(fileLocator, true, properties);
  }

  public AutoFileConfigurationResolver(
      FileLocator fileLocator) {
    this(fileLocator, Collections.emptyMap());
  }

  @Override
  public void reload() {
    URL url = fileLocationStrategy.locate(fileSystem, fileLocator);
    if (url != null) {
      try (InputStream inputStream = url.openStream()) {
        update(configurationParserSelector.parse(url.toURI(), inputStream));
      } catch (IOException | URISyntaxException e) {
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
