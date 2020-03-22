package com.dadazhishi.zheng.redis;

import static org.junit.Assert.assertEquals;

import com.github.fppt.jedismock.RedisServer;
import com.google.inject.Guice;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import java.io.IOException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LettuceModuleTest {

  private RedisServer server = null;

  @Before
  public void before() throws IOException {
    server = RedisServer.newRedisServer();  // bind to a random port
    server.start();
  }

  @Test
  public void test() {
    RedisClient jedis = Guice
        .createInjector(new LettuceModule(server.getHost(), server.getBindPort()))
        .getInstance(RedisClient.class);
    StatefulRedisConnection<String, String> connection = jedis.connect();
    RedisCommands<String, String> commands = connection.sync();
    commands.set("test", "zheng");
    System.out.println(commands.info());
    assertEquals("zheng", commands.get("test"));
  }

  @After
  public void after() {
    server.stop();
    server = null;
  }
}