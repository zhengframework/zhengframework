package com.github.zhengframework.configuration.reload;

public interface ReloadStrategy {

  void register(Reloadable resource);

  void deregister(Reloadable resource);
}
