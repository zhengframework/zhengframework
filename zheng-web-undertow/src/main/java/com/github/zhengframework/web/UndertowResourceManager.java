package com.github.zhengframework.web;

import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.server.handlers.resource.Resource;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class UndertowResourceManager extends ClassPathResourceManager {

  public static final Set<String> DEFAULT_WHITELIST = new HashSet<>();

  static {

    DEFAULT_WHITELIST.add(".css");
    DEFAULT_WHITELIST.add(".js");
    DEFAULT_WHITELIST.add(".jpg");
    DEFAULT_WHITELIST.add(".gif");
    DEFAULT_WHITELIST.add(".jpeg");
    DEFAULT_WHITELIST.add(".json");
    DEFAULT_WHITELIST.add(".woff");
    DEFAULT_WHITELIST.add(".woff2");
    DEFAULT_WHITELIST.add(".svg");
    DEFAULT_WHITELIST.add(".ttf");
    DEFAULT_WHITELIST.add(".eot");
    DEFAULT_WHITELIST.add(".png");
    DEFAULT_WHITELIST.add(".html");
    DEFAULT_WHITELIST.add(".htm");
  }

  private Set<String> whitelist = new HashSet<>();

  public UndertowResourceManager(ClassLoader classLoader, String prefix, Set<String> whitelist) {
    super(classLoader, prefix);
    this.whitelist = whitelist;
  }

  public UndertowResourceManager(ClassLoader classLoader) {
    this(classLoader, "META-INF/resources/", DEFAULT_WHITELIST);
  }

  public UndertowResourceManager() {
    this(Thread.currentThread().getContextClassLoader(), "META-INF/resources/", DEFAULT_WHITELIST);
  }

  @Override
  public Resource getResource(String path) throws IOException {
    String pathExt = null;
    if (path.indexOf('.') >= 0) {
      pathExt = path.substring(path.lastIndexOf('.'));
    } else {
      pathExt = path;
    }

    if (whitelist.contains(pathExt.toLowerCase())) {
      return super.getResource(path);
    }
    throw new IOException("Not able to read resource : " + path);
  }
}
