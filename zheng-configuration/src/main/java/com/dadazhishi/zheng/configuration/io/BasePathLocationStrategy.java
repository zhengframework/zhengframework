package com.github.zhengframework.configuration.io;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class BasePathLocationStrategy implements FileLocationStrategy {

  @Override
  public URL locate(FileSystem fileSystem, FileLocator locator) {
    if (StringUtils.isNotEmpty(locator.getFileName())) {
      final File file = new File(locator.getBasePath(), locator.getFileName());
      if (file.isFile()) {
        try {
          log.debug("Loading configuration from the file ({})", file.getCanonicalPath());
          return file.toURI().toURL();
        } catch (IOException e) {
          return null;
        }
      }
    }
    return null;
  }
}
