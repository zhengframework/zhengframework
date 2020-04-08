package com.dadazhishi.zheng.configuration.reload;

public interface ReloadStrategy {

  void register(Reloadable resource);

  void deregister(Reloadable resource);
}
