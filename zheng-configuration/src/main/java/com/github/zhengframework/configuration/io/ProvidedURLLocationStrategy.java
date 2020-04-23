package com.github.zhengframework.configuration.io;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

public class ProvidedURLLocationStrategy implements FileLocationStrategy {

  @Override
  public Optional<URL> locate(FileSystem fileSystem, FileLocator locator) {
    try {
      return Optional.of(new URL(locator.getSourceURL()));
    } catch (MalformedURLException e) {
      return Optional.empty();
    }
  }
}
