package com.dadazhishi.zheng.memcached;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import java.io.IOException;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.XMemcachedClient;

public class MemcachedModule extends AbstractModule {

  private String host;
  private int port;

  public MemcachedModule(String host, int port) {
    this.host = host;
    this.port = port;
  }

  @Provides
  public MemcachedClient memcachedClient() throws IOException {
    XMemcachedClient client = new XMemcachedClient(host, port);
    return client;
  }

}
