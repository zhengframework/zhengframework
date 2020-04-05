package com.dadazhishi.zheng.web;

public class PathUtils {

  public static String fixPath(String path) {
    if ("/".equals(path)) {
      path = null;
    } else {
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
