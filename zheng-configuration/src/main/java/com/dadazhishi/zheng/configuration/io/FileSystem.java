package com.github.zhengframework.configuration.io;

import com.github.zhengframework.configuration.ex.ConfigurationException;
import java.io.InputStream;
import java.net.URL;

public abstract class FileSystem {

  private volatile FileOptionsProvider optionsProvider;

  public FileOptionsProvider getFileOptionsProvider() {
    return this.optionsProvider;
  }

  public void setFileOptionsProvider(final FileOptionsProvider provider) {
    this.optionsProvider = provider;
  }

  public abstract InputStream getInputStream(URL url) throws ConfigurationException;

  public abstract URL locateFromURL(String basePath, String fileName);
}
