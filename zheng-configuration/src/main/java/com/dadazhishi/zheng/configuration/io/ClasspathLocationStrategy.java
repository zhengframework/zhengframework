package com.github.zhengframework.configuration.io;

import java.net.URL;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClasspathLocationStrategy implements FileLocationStrategy {

  @Override
  public URL locate(FileSystem fileSystem, FileLocator locator) {
    ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
    URL resource = contextClassLoader.getResource(locator.getFileName());
    if (resource != null) {
      log.debug("Loading configuration from the context classpath ({})", locator.getFileName());
      return resource;
    }
    ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
    resource = systemClassLoader.getResource(locator.getFileName());
    if (resource != null) {
      log.debug("Loading configuration from the system classpath ({})", locator.getFileName());
      return resource;
    }
    return null;
  }
}
