package com.github.zhengframework.configuration.reload;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ImmediateReloadStrategy implements ReloadStrategy {

  @Override
  public void register(Reloadable resource) {
    log.debug("Registering resource " + resource);
    resource.reload();
  }

  @Override
  public void deregister(Reloadable resource) {
    log.debug("De-registering resource " + resource);
  }
}
