package com.dadazhishi.zheng.redis;

import static org.junit.Assert.assertEquals;

import com.github.fppt.jedismock.RedisServer;
import com.google.inject.Guice;
import java.io.IOException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

public class RedisModuleTest {

  private RedisServer server = null;

  @Before
  public void before() throws IOException {
    server = RedisServer.newRedisServer();  // bind to a random port
    server.start();
  }

  @Test
  public void test() {
    Jedis jedis = Guice.createInjector(new RedisModule(server.getHost(), server.getBindPort()))
        .getInstance(Jedis.class);
    jedis.set("test", "zheng");
    System.out.println(jedis.info());
    assertEquals("zheng", jedis.get("test"));
  }

  @After
  public void after() {
    server.stop();
    server = null;
  }
}