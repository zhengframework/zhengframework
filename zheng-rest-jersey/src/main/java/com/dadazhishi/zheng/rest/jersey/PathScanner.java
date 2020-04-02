package com.dadazhishi.zheng.rest.jersey;

import com.dadazhishi.zheng.service.Scanner;
import com.google.inject.Injector;
import javax.ws.rs.Path;

public class PathScanner extends Scanner<Path> {

  public PathScanner(Injector injector) {
    super(injector, Path.class);
  }
}
