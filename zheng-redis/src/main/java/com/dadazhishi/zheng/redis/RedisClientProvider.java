package com.github.zhengframework.redis;

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
    builder.withHost(redisConfig.getHost())
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
