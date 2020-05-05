package com.github.zhengframework.memcached;

import static org.junit.Assert.assertEquals;

import com.github.zhengframework.test.WithZhengApplication;
import com.github.zhengframework.test.ZhengApplicationRunner;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.thimbleware.jmemcached.CacheImpl;
import com.thimbleware.jmemcached.Key;
import com.thimbleware.jmemcached.LocalCacheElement;
import com.thimbleware.jmemcached.MemCacheDaemon;
import com.thimbleware.jmemcached.storage.CacheStorage;
import com.thimbleware.jmemcached.storage.hash.ConcurrentLinkedHashMap;
import java.net.InetSocketAddress;
import net.rubyeye.xmemcached.MemcachedClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(ZhengApplicationRunner.class)
public class MemcachedModuleTest {

  private static final int DEFAULT_STORAGE_CAPACITY = 1000;
  private static final long DEFAULT_STORAGE_MEMORY_CAPACITY = 10000;
  String host = "127.0.0.1";
  int port;
  private MemCacheDaemon<LocalCacheElement> memcacheDaemon;
  @Inject
  private Injector injector;

  @Before
  public void setup() {
    //    port = FreePortFinder.findFreeLocalPort();
    port = 11211;
    CacheStorage<Key, LocalCacheElement> storage =
        ConcurrentLinkedHashMap.create(
            ConcurrentLinkedHashMap.EvictionPolicy.FIFO,
            DEFAULT_STORAGE_CAPACITY,
            DEFAULT_STORAGE_MEMORY_CAPACITY);
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
  @WithZhengApplication
  public void memcachedClient() throws Exception {
    MemcachedClient client = injector.getInstance(MemcachedClient.class);
    client.set("key1", 3600, "hello");
    Object someObject = client.get("key1");
    assertEquals("hello", someObject);
    client.delete("key1");
    client.shutdown();
  }
}
