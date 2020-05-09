package com.github.zhengframework.remoteconfig;

public class DefaultRemoteConfigExceptionMapper implements RemoteConfigExceptionMapper {

  @Override
  public RemoteConfigResponse<?> resolve(Exception exception) {
    if (exception instanceof RemoteConfigException) {
      RemoteConfigException remoteConfigException = (RemoteConfigException) exception;
      return remoteConfigException.getResponse();
    } else {
      return RemoteConfigResponse.builder()
          .success(false)
          .message(exception.getMessage())
          .build();
    }
  }
}
