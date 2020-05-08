package com.github.zhengframework.redis;

import static com.google.inject.name.Names.named;
import static org.junit.Assert.assertEquals;

import com.github.fppt.jedismock.RedisServer;
import com.github.zhengframework.test.WithZhengApplication;
import com.github.zhengframework.test.ZhengApplicationRunner;
import com.google.inject.Injector;
import com.google.inject.Key;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import java.io.IOException;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@Slf4j
@RunWith(ZhengApplicationRunner.class)
public class RedisModuleTest {

  private RedisServer server = null;
  @Inject private Injector injector;

  @Before
  public void before() throws IOException {
    server = RedisServer.newRedisServer(17981);
    server.start();
  }

  @Test
  @WithZhengApplication()
  public void test() {
    RedisClient redisClient = injector.getInstance(RedisClient.class);
    StatefulRedisConnection<String, String> connection = redisClient.connect();
    RedisCommands<String, String> commands = connection.sync();
    commands.set("test", "zheng");
    log.info("{}", commands.info());
    assertEquals("zheng", commands.get("test"));
  }

  @Test
  @WithZhengApplication(configFile = "application_group.properties")
  public void testGroup() {
    RedisClient redisClient = injector.getInstance(Key.get(RedisClient.class, named("a1")));
    StatefulRedisConnection<String, String> connection = redisClient.connect();
    RedisCommands<String, String> commands = connection.sync();
    commands.set("test", "zheng");
    log.info("{}", commands.info());
    assertEquals("zheng", commands.get("test"));
  }

  @After
  public void after() {
    server.stop();
    server = null;
  }
}
