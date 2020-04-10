package com.github.zhengframework.configuration.io;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class HomeDirectoryLocationStrategy implements FileLocationStrategy {

  private static final String PROP_HOME = "user.home";
  private final String homeDirectory;
  private boolean withBasePath;

  public HomeDirectoryLocationStrategy(String homeDir, final boolean withBasePath) {
    this.homeDirectory = fetchHomeDirectory(homeDir);
    this.withBasePath = withBasePath;
  }

  public HomeDirectoryLocationStrategy(boolean withBasePath) {
    this(null, withBasePath);
  }

  public HomeDirectoryLocationStrategy() {
    this(null, false);
  }

  private static String fetchHomeDirectory(final String homeDir) {
    return homeDir != null ? homeDir : System.getProperty(PROP_HOME);
  }

  @Override
  public URL locate(FileSystem fileSystem, FileLocator locator) {
    if (StringUtils.isNotEmpty(locator.getFileName())) {
      final String basePath = fetchBasePath(locator);
      final File file = new File(basePath, locator.getFileName());
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

  private String fetchBasePath(final FileLocator locator) {
    if (withBasePath
        && StringUtils.isNotEmpty(locator.getBasePath())) {

      final StringBuilder fName = new StringBuilder();
      fName.append(homeDirectory);
      if (!homeDirectory.endsWith(File.separator)) {
        fName.append(File.separator);
      }
      String ext = locator.getBasePath();
      if (ext.startsWith("." + File.separator)) {
        fName.append(ext.substring(2));
      } else {
        fName.append(ext);
      }
      return fName.toString();
    }
    return homeDirectory;
  }
}
