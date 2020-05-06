package com.github.zhengframework.healthcheck.http;

/*-
 * #%L
 * zheng-healthcheck
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

import java.util.Objects;

public class HttpHealthResponse {

  private final int status;
  private final String message;
  private final String body;

  public HttpHealthResponse(final int status, String message, final String body) {
    this.status = status;
    this.message = message;
    this.body = Objects.requireNonNull(body);
  }

  public String getMessage() {
    return message;
  }

  public int getStatus() {
    return status;
  }

  public String getBody() {
    return body;
  }

  @Override
  public boolean equals(final Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof HttpHealthResponse)) {
      return false;
    }
    final HttpHealthResponse that = (HttpHealthResponse) other;
    return status == that.status && message.equals(that.message) && Objects.equals(body, that.body);
  }

  @Override
  public int hashCode() {
    return Objects.hash(status, body);
  }

  @Override
  public String toString() {
    return "HttpHealthResponse{status="
        + status
        + "message="
        + message
        + ", body='"
        + body
        + '\''
        + '}';
  }
}
