package com.github.zhengframework.core;

public interface Service {

  /**
   * @return Service will start by order [1,0,-1]
   */
  int priority();

  void start() throws Exception;

  void stop() throws Exception;

}