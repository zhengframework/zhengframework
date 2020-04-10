package com.github.zhengframework.web;

import com.google.inject.Injector;
import javax.inject.Provider;

public interface WebServer {

  void init(Provider<WebConfig> webConfigProvider, Provider<Injector> injectorProvider);

  void start() throws Exception;

  void stop() throws Exception;
}
