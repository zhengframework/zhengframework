package com.github.zhengframework.remoteconfig;

/*-
 * #%L
 * zheng-remote-config
 * %%
 * Copyright (C) 2020 Zheng MingHai
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
// @Builder
public class RemoteConfigResponse<T> {

  @Getter private T data;
  @Getter private boolean success = true;
  @Getter private String message;

  public static <T> RemoteConfigResponseBuilder<T> builder() {
    return new RemoteConfigResponseBuilder<>();
  }

  public static final class RemoteConfigResponseBuilder<T> {

    private T data;
    private boolean success = true;
    private String message;

    private RemoteConfigResponseBuilder() {}

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
