package com.dadazhishi.zheng.redis;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;

public class LettuceModule extends AbstractModule {

  private String host;
  private int port;

  public LettuceModule(String host, int port) {
    this.host = host;
    this.port = port;
  }

  public LettuceModule(String host) {
    this(host, 6379);
  }

  @Override
  protected void configure() {
  }

  @Provides
  public RedisClient redisClient() {
    RedisURI redisURI = RedisURI.builder()
        .withHost(host)
        .withPort(port)
        .build();
    return RedisClient.create(redisURI);
  }
}
