package com.dadazhishi.zheng.configuration.io;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class AbsoluteNameLocationStrategy implements FileLocationStrategy {

  @Override
  public URL locate(final FileSystem fileSystem, final FileLocator locator) {
    if (StringUtils.isNotEmpty(locator.getFileName())) {
      final File file = new File(locator.getFileName());
      if (file.isAbsolute() && file.exists()) {
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
