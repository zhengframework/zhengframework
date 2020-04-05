package com.dadazhishi.zheng.configuration.io;

import com.google.common.base.Preconditions;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;

public class CombinedLocationStrategy implements FileLocationStrategy {

  private final Collection<FileLocationStrategy> subStrategies;

  public CombinedLocationStrategy(
      Collection<FileLocationStrategy> subStrategies) {
    Preconditions
        .checkState(subStrategies != null, "Collection with sub strategies must not be null!");
    this.subStrategies = Collections.unmodifiableCollection(subStrategies);
  }

  @Override
  public URL locate(FileSystem fileSystem, FileLocator locator) {
    for (final FileLocationStrategy sub : subStrategies) {
      final URL url = sub.locate(fileSystem, locator);
      if (url != null) {
        return url;
      }
    }
    return null;
  }
}
