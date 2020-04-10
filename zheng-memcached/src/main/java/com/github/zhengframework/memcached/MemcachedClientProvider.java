package com.github.zhengframework.memcached;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.auth.AuthInfo;
import net.rubyeye.xmemcached.utils.AddrUtil;

@Singleton
public class MemcachedClientProvider implements Provider<MemcachedClient> {

  private final MemcachedConfig memcachedConfig;

  @Inject
  public MemcachedClientProvider(MemcachedConfig memcachedConfig) {
    this.memcachedConfig = memcachedConfig;
  }

  @Override
  public MemcachedClient get() {
    List<InetSocketAddress> addresses = AddrUtil.getAddresses(memcachedConfig.getAddresses());
    XMemcachedClientBuilder builder = new XMemcachedClientBuilder(addresses);
    if (memcachedConfig.getAuthType() != null) {
      AuthInfo authInfo = null;
      if (AuthType.PLAIN == memcachedConfig.getAuthType()) {
        authInfo = AuthInfo.plain(memcachedConfig.getUsername(), memcachedConfig.getPassword());
      } else if (AuthType.CRAM_MD5 == memcachedConfig.getAuthType()) {
        authInfo = AuthInfo.cramMD5(memcachedConfig.getUsername(), memcachedConfig.getPassword());
      } else if (AuthType.TYPICAL == memcachedConfig.getAuthType()) {
        authInfo = AuthInfo.typical(memcachedConfig.getUsername(), memcachedConfig.getPassword());
      }
      Map<InetSocketAddress, AuthInfo> map = new HashMap<>();
      for (InetSocketAddress address : addresses) {
        map.put(address, authInfo);
      }
      builder.setAuthInfoMap(map);
    }
    try {
      return builder.build();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
