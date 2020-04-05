package com.dadazhishi.zheng.configuration.io;

import com.dadazhishi.zheng.configuration.ex.ConfigurationException;
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
