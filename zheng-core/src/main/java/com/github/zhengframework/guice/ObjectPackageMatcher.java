package com.github.zhengframework.guice;

import com.google.inject.TypeLiteral;
import com.google.inject.matcher.AbstractMatcher;

public class ObjectPackageMatcher<T> extends AbstractMatcher<T> {

  private final String pkg;

  public ObjectPackageMatcher(final String pkg) {
    this.pkg = pkg;
  }

  @Override
  public boolean matches(final T o) {
    final Class<?> type = o instanceof TypeLiteral ? ((TypeLiteral) o).getRawType() : o.getClass();
    return Utils.isPackageValid(type) && type.getPackage().getName().startsWith(pkg);
  }
}
