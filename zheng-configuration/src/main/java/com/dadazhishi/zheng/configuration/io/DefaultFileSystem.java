package com.dadazhishi.zheng.configuration.io;

import com.dadazhishi.zheng.configuration.ex.ConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

@Slf4j
public class DefaultFileSystem extends FileSystem {

  @Override
  public InputStream getInputStream(URL url) throws ConfigurationException {
    final File file = FileUtils.toFile(url);
    if (file != null && file.isDirectory()) {
      throw new ConfigurationException("Cannot load a configuration from a directory");
    }
    try {
      return url.openStream();
    } catch (final Exception e) {
      throw new ConfigurationException("Unable to load the configuration from the URL " + url, e);
    }
  }

  @Override
  public URL locateFromURL(String basePath, String fileName) {
    try {
      URL url;
      if (basePath == null) {
        return new URL(fileName);
      }
      final URL baseURL = new URL(basePath);
      url = new URL(baseURL, fileName);

      InputStream in = null;
      try {
        in = url.openStream();
      } finally {
        if (in != null) {
          in.close();
        }
      }
      return url;
    } catch (IOException e) {
      log.debug("Could not locate file {} at {}: {}", fileName, basePath, e.getMessage());
      return null;
    }
  }

}
