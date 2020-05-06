package com.github.zhengframework.memcached;

/*-
 * #%L
 * zheng-memcached
 * %%
 * Copyright (C) 2020 Zheng MingHai
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
