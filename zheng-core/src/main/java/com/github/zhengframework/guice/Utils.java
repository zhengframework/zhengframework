package com.github.zhengframework.guice;

import java.lang.reflect.Method;

@SuppressWarnings("rawtypes")
class Utils {

  public static boolean isPackageValid(final Class type) {
    boolean res = false;
    if (type != null) {
      final Package pack = type.getPackage();
      res = !(pack == null || pack.getName().startsWith("java"));
    }
    return res;
  }

  public static void checkNoParams(final Method method) {
    if (method.getParameterTypes().length > 0) {
      throw new IllegalStateException("Method without parameters required");
    }
  }
}
