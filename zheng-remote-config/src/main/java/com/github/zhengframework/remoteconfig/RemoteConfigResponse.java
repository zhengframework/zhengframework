package com.github.zhengframework.remoteconfig;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
//@Builder
public class RemoteConfigResponse<T> {

  @Getter
  private T data;
  @Getter
  private boolean success = true;
  @Getter
  private String message;

  public static <T> RemoteConfigResponseBuilder<T> builder() {
    return new RemoteConfigResponseBuilder<>();
  }

  public static final class RemoteConfigResponseBuilder<T> {

    private T data;
    private boolean success = true;
    private String message;

    private RemoteConfigResponseBuilder() {
    }

    public RemoteConfigResponseBuilder<T> data(T data) {
      this.data = data;
      return this;
    }

    public RemoteConfigResponseBuilder<T> success(boolean success) {
      this.success = success;
      return this;
    }

    public RemoteConfigResponseBuilder<T> message(String message) {
      this.message = message;
      return this;
    }

    public RemoteConfigResponse<T> build() {
      return new RemoteConfigResponse<T>(data, success, message);
    }
  }

//  public static RemoteConfigResponseBuilder<?> success() {
//    return RemoteConfigResponse.builder().success(true);
//  }
//
//  public static RemoteConfigResponseBuilder<?> failure() {
//    return RemoteConfigResponse.builder().success(false);
//  }
}
