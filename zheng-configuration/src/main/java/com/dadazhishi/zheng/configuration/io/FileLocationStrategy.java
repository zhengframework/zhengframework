package com.dadazhishi.zheng.configuration.io;

import java.net.URL;

public interface FileLocationStrategy {

  URL locate(FileSystem fileSystem, FileLocator locator);
}
