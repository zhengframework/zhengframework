package com.github.zhengframework.service;

public interface Service {

  /**
   * @return Service will start by order [1,0,-1]
   */
  int order();

  void start() throws Exception;

  void stop() throws Exception;

}