package com.github.zhengframework.remoteconfig;

public class RemoteConfigException extends Exception {

  private static final long serialVersionUID = 2804131287746588562L;
  private final RemoteConfigResponse<?> response;

  public RemoteConfigException(RemoteConfigResponse<?> response) {
    this.response = response;
  }

  public RemoteConfigResponse<?> getResponse() {
    return response;
  }

}
