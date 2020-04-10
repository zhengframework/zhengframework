package com.github.zhengframework.configuration.io;

import java.net.URL;

public interface FileLocationStrategy {

  URL locate(FileSystem fileSystem, FileLocator locator);
}
