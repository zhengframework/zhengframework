package com.github.zhengframework.configuration.io;

import java.net.URL;
import java.util.Optional;

public class FileSystemLocationStrategy implements FileLocationStrategy {

  @Override
  public Optional<URL> locate(FileSystem fileSystem, FileLocator locator) {
    return Optional.ofNullable(fileSystem.locateFromURL(locator.getBasePath(),
        locator.getFileName()));
  }
}
