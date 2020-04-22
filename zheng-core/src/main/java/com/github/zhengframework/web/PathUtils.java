package com.github.zhengframework.web;

import com.google.common.base.Joiner;

public class PathUtils {

  public static String fixPath(String... paths) {
    String path = Joiner.on("/").skipNulls().join(paths);
    path = path.replaceAll("[/]+", "/");
    if ("/".equals(path)) {
      path = "";
    } else if(path.length() > 1) {
      if (path.endsWith("/")) {
        path = path.substring(0, path.length() - 1);
      }
      if (!path.startsWith("/")) {
        path = "/" + path;
      }
    }
    return path;
  }

}
