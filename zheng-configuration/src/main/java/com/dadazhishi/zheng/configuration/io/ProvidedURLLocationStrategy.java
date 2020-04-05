package com.dadazhishi.zheng.configuration.io;

import java.net.URL;

public class ProvidedURLLocationStrategy implements FileLocationStrategy {

  @Override
  public URL locate(FileSystem fileSystem, FileLocator locator) {
    return locator.getSourceURL();
  }
}
