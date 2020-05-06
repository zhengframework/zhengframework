package com.github.zhengframework.redis;

/*-
 * #%L
 * zheng-redis
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

import com.google.common.base.Strings;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.RedisURI.Builder;
import java.time.Duration;
import javax.inject.Inject;
import javax.inject.Provider;

public class RedisClientProvider implements Provider<RedisClient> {

  private final RedisConfig redisConfig;

  @Inject
  public RedisClientProvider(RedisConfig redisConfig) {
    this.redisConfig = redisConfig;
  }

  @Override
  public RedisClient get() {
    Builder builder = RedisURI.builder();
    builder
        .withHost(redisConfig.getHost())
        .withPort(redisConfig.getPort())
        .withDatabase(redisConfig.getDatabase())
        .withTimeout(Duration.ofSeconds(redisConfig.getTimeout()));
    if (redisConfig.getSsl() != null) {
      builder.withSsl(redisConfig.getSsl());
    }
    if (redisConfig.getStartTls() != null) {
      builder.withSsl(redisConfig.getStartTls());
    }
    if (redisConfig.getVerifyPeer() != null) {
      builder.withSsl(redisConfig.getVerifyPeer());
    }
    if (!Strings.isNullOrEmpty(redisConfig.getPassword())) {
      builder.withPassword(redisConfig.getPassword());
    }
    RedisURI redisURI = builder.build();
    return RedisClient.create(redisURI);
  }
}
