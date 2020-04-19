package com.github.zhengframework.healthcheck.tcp;

import com.github.zhengframework.healthcheck.NamedHealthCheck;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.time.Duration;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TcpHealthCheck extends NamedHealthCheck {

  private static final Duration DEFAULT_CONNECTION_TIMEOUT = Duration.ofSeconds(2);
  private final String host;
  private final int port;
  private final Duration connectionTimeout;

  public TcpHealthCheck(String host, int port, Duration connectionTimeout) {
    this.host = Objects.requireNonNull(host);
    this.port = port;
    Preconditions.checkState(!connectionTimeout.isNegative(),
        "connectionTimeout must be a non-negative value.");
    Preconditions.checkState(connectionTimeout.toMillis() <= Integer.MAX_VALUE,
        "Cannot configure a connectionTimeout greater than the max integer value");
    this.connectionTimeout = connectionTimeout;
  }

  public TcpHealthCheck(final String host,
      final int port) {
    this(host, port, DEFAULT_CONNECTION_TIMEOUT);
  }

  @Override
  public String getName() {
    return String.format("TcpHealthCheck-%s:%d", host, port);
  }

  @Override
  protected Result check() throws Exception {
    final boolean isHealthy = tcpCheck(host, port);

    if (isHealthy) {
      log.debug("Health check against url={}:{} successful", host, port);
      return Result.healthy();
    }

    log.debug("Health check against url={}:{} failed", host, port);
    return Result.unhealthy("TCP health check against host=%s port=%s failed", host, port);
  }

  protected boolean tcpCheck(final String host, final int port) throws IOException {
    try (Socket socket = new Socket()) {
      socket.connect(new InetSocketAddress(host, port), (int) connectionTimeout.toMillis());
      return socket.isConnected();
    }
  }
}
