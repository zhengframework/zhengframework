package com.github.zhengframework.healthcheck.http;

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
    return status == that.status && message.equals(that.message)
        && Objects.equals(body, that.body);
  }

  @Override
  public int hashCode() {
    return Objects.hash(status, body);
  }

  @Override
  public String toString() {
    return "HttpHealthResponse{status=" + status
        + "message=" + message
        + ", body='" + body + '\''
        + '}';
  }
}
