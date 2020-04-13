package com.github.zhengframework.memcached;

import static org.junit.Assert.assertEquals;

import com.github.zhengframework.bootstrap.ZhengApplication;
import com.github.zhengframework.bootstrap.ZhengApplicationBuilder;
import com.github.zhengframework.configuration.Configuration;
import com.github.zhengframework.configuration.MapConfiguration;
import com.thimbleware.jmemcached.CacheImpl;
import com.thimbleware.jmemcached.Key;
import com.thimbleware.jmemcached.LocalCacheElement;
import com.thimbleware.jmemcached.MemCacheDaemon;
import com.thimbleware.jmemcached.storage.CacheStorage;
import com.thimbleware.jmemcached.storage.hash.ConcurrentLinkedHashMap;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import me.alexpanov.net.FreePortFinder;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MemcachedModuleTest {

  private static final int DEFAULT_STORAGE_CAPACITY = 1000;
  private static final long DEFAULT_STORAGE_MEMORY_CAPACITY = 10000;
  String host = "127.0.0.1";
  int port;
  private MemCacheDaemon<LocalCacheElement> memcacheDaemon;

  @Before
  public void setup() {

    port = FreePortFinder.findFreeLocalPort();

    CacheStorage<Key, LocalCacheElement> storage = ConcurrentLinkedHashMap
        .create(ConcurrentLinkedHashMap
            .EvictionPolicy.FIFO, DEFAULT_STORAGE_CAPACITY, DEFAULT_STORAGE_MEMORY_CAPACITY);
    memcacheDaemon = new MemCacheDaemon<>();
    memcacheDaemon.setCache(new CacheImpl(storage));
    memcacheDaemon.setVerbose(true);
    memcacheDaemon.setBinary(false);
    memcacheDaemon.setAddr(new InetSocketAddress(host, port));
    memcacheDaemon.start();
  }

  @After
  public void stop() {
    memcacheDaemon.getCache().flush_all();
    memcacheDaemon.stop();
  }

  @Test
  public void memcachedClient()
      throws InterruptedException, MemcachedException, TimeoutException, IOException {
    Map<String, String> map = new HashMap<>();
    map.put("zheng.memcached.addresses", String.format("%s:%d", host, port));
    Configuration configuration = new MapConfiguration(map);

    ZhengApplication application = ZhengApplicationBuilder.create()
        .enableAutoLoadModule()
        .withConfiguration(configuration)
        .build();
    MemcachedClient client = application.getInjector().getInstance(MemcachedClient.class);
    client.set("key1", 3600, "hello");
    Object someObject = client.get("key1");
    assertEquals("hello", someObject);
    client.delete("key1");
    client.shutdown();
  }
}