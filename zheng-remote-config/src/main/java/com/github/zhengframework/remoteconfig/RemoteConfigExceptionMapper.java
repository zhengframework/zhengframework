package com.github.zhengframework.remoteconfig;

public interface RemoteConfigExceptionMapper {

  RemoteConfigResponse<?> resolve(Exception exception);

}
