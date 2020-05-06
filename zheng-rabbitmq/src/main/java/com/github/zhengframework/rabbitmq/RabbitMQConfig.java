package com.github.zhengframework.rabbitmq;

/*-
 * #%L
 * zheng-rabbitmq
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

import com.github.zhengframework.configuration.ConfigurationDefine;
import com.github.zhengframework.configuration.annotation.ConfigurationInfo;
import com.rabbitmq.client.ConnectionFactory;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.kohsuke.MetaInfServices;

@MetaInfServices
@Data
@NoArgsConstructor
@ConfigurationInfo(prefix = "zheng.rabbitmq", supportGroup = true)
public class RabbitMQConfig implements ConfigurationDefine {

  public static final String PREFIX = "zheng.rabbitmq";
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
