package com.github.zhengframework.configuration.io;

import java.net.URL;

public class FileSystemLocationStrategy implements FileLocationStrategy {

  @Override
  public URL locate(FileSystem fileSystem, FileLocator locator) {
    return fileSystem.locateFromURL(locator.getBasePath(),
        locator.getFileName());
  }
}
