package com.dadazhishi.zheng.jersey;

public interface AppServer {

  void start() throws Exception;

  void join() throws Exception;

  void stop() throws Exception;
}
