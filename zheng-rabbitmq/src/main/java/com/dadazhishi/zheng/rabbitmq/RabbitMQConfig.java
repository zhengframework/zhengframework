package com.github.zhengframework.rabbitmq;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.zhengframework.configuration.spi.ConfigurationDefine;
import com.rabbitmq.client.ConnectionFactory;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
public class RabbitMQConfig implements ConfigurationDefine {

  public static final String PREFIX = "zheng.rabbitmq";
  private boolean group = false;
  private String virtualHost = ConnectionFactory.DEFAULT_VHOST;
  private String host = ConnectionFactory.DEFAULT_HOST;
  private int port = ConnectionFactory.USE_DEFAULT_PORT;
  private String username = ConnectionFactory.DEFAULT_USER;
  private String password = ConnectionFactory.DEFAULT_PASS;
  private int requestedChannelMax = ConnectionFactory.DEFAULT_CHANNEL_MAX;
  private int requestedFrameMax = ConnectionFactory.DEFAULT_FRAME_MAX;
  private int requestedHeartbeat = ConnectionFactory.DEFAULT_HEARTBEAT;
  private long networkRecoveryInterval = ConnectionFactory.DEFAULT_NETWORK_RECOVERY_INTERVAL;
  private String url;
  private boolean ssl = false;
  private boolean automaticRecovery = true;
  private boolean topologyRecovery = true;
  private int connectionTimeout = ConnectionFactory.DEFAULT_CONNECTION_TIMEOUT;
  private int handshakeTimeout = ConnectionFactory.DEFAULT_HANDSHAKE_TIMEOUT;
  private int shutdownTimeout = ConnectionFactory.DEFAULT_SHUTDOWN_TIMEOUT;
  private int channelRpcTimeout = ConnectionFactory.DEFAULT_CHANNEL_RPC_TIMEOUT;
  private int workPoolTimeout = ConnectionFactory.DEFAULT_WORK_POOL_TIMEOUT;

}
