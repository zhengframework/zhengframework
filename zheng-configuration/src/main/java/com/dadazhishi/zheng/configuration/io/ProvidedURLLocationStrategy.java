package com.github.zhengframework.configuration.io;

import java.net.MalformedURLException;
import java.net.URL;

public class ProvidedURLLocationStrategy implements FileLocationStrategy {

  @Override
  public URL locate(FileSystem fileSystem, FileLocator locator) {
    try {
      return new URL(locator.getSourceURL());
    } catch (MalformedURLException e) {
      return null;
    }
  }
}
