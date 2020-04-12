package com.github.zhengframework.guice;

import java.lang.reflect.Method;

public class Utils {

  public static boolean isPackageValid(final Class type) {
    boolean res = false;
    if (type != null) {
      final Package packaj = type.getPackage();
      res = !(packaj == null || packaj.getName().startsWith("java"));
    }
    return res;
  }

  public static void checkNoParams(final Method method) {
    if (method.getParameterTypes().length > 0) {
      throw new IllegalStateException("Method without parameters required");
    }
  }
}
