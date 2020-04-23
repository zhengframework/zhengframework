package com.github.zhengframework.configuration.io;

import java.net.URL;
import java.util.Optional;

public interface FileLocationStrategy {

  Optional<URL> locate(FileSystem fileSystem, FileLocator locator);
}
