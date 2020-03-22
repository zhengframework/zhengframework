package com.dadazhishi.zheng.jersey.grizzly2;

import org.glassfish.grizzly.http.server.HttpServer;

public interface GrizzlyServerCreator {

  HttpServer create();
}
