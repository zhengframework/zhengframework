package com.github.zhengframework.remoteconfig;


public class CustomRemoteConfigExceptionMapper extends DefaultRemoteConfigExceptionMapper {

  @Override
  public RemoteConfigResponse<?> resolve(Exception exception) {
    if (exception instanceof IllegalAccessException) {
      return RemoteConfigResponse.builder().success(false)
          .message("CustomRemoteConfigExceptionMapper").build();
    } else {
      return super.resolve(exception);
    }
  }
}
