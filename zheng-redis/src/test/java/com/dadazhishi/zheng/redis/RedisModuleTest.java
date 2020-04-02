package com.dadazhishi.zheng.redis;

import static com.google.inject.name.Names.named;
import static org.junit.Assert.assertEquals;

import com.dadazhishi.zheng.configuration.NamespaceConfiguration;
import com.dadazhishi.zheng.service.ZhengApplication;
import com.github.fppt.jedismock.RedisServer;
import com.google.inject.Key;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RedisModuleTest {

  private RedisServer server = null;

  @Before
  public void before() throws IOException {
    server = RedisServer.newRedisServer();  // bind to a random port
    server.start();
  }

  @Test
  public void test() {
    Map<String, String> map = new HashMap<>();
    map.put("zheng.redis.host", server.getHost());
    map.put("zheng.redis.port", "" + server.getBindPort());
    System.out.println(map);
    NamespaceConfiguration configuration = new NamespaceConfiguration(map);

    ZhengApplication application = ZhengApplication.create(configuration, new RedisModule());
    RedisClient redisClient = application.getInjector()
        .getInstance(RedisClient.class);
    StatefulRedisConnection<String, String> connection = redisClient.connect();
    RedisCommands<String, String> commands = connection.sync();
    commands.set("test", "zheng");
    System.out.println(commands.info());
    assertEquals("zheng", commands.get("test"));
  }

  @Test
  public void testGroup() {
    Map<String, String> map = new HashMap<>();
    map.put("zheng.redis.group", "true");
    map.put("zheng.redis.a1.host", server.getHost());
    map.put("zheng.redis.a1.port", "" + server.getBindPort());
    map.put("zheng.redis.a2.host", server.getHost());
    map.put("zheng.redis.a2.port", "" + server.getBindPort());
    System.out.println(map);
    NamespaceConfiguration configuration = new NamespaceConfiguration(map);

    ZhengApplication application = ZhengApplication.create(configuration, new RedisModule());
    RedisClient redisClient = application.getInjector()
        .getInstance(Key.get(RedisClient.class, named("a1")));
    StatefulRedisConnection<String, String> connection = redisClient.connect();
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