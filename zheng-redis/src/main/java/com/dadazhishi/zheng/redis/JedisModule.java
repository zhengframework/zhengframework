package com.dadazhishi.zheng.redis;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import redis.clients.jedis.Jedis;

public class JedisModule extends AbstractModule {

  private String host;
  private int port;

  public JedisModule(String host, int port) {
    this.host = host;
    this.port = port;
  }

  public JedisModule(String host) {
    this(host, 6379);
  }

  @Override
  protected void configure() {
  }

  @Provides
  public Jedis jedis() {
    return new Jedis(host, port);
  }
}
