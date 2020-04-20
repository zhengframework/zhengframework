package com.github.zhengframework.configuration.io;

import com.google.common.base.Preconditions;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public class CombinedLocationStrategy implements FileLocationStrategy {

  private final Collection<FileLocationStrategy> subStrategies;

  public CombinedLocationStrategy(
      Collection<FileLocationStrategy> subStrategies) {
    Preconditions
        .checkState(subStrategies != null, "Collection with sub strategies must not be null!");
    this.subStrategies = Collections.unmodifiableCollection(subStrategies);
  }

  @Override
  public Optional<URL> locate(FileSystem fileSystem, FileLocator locator) {
    for (final FileLocationStrategy sub : subStrategies) {
      Optional<URL> optional = sub.locate(fileSystem, locator);
      if (optional.isPresent()) {
        return optional;
      }
    }
    return Optional.empty();
  }
}
