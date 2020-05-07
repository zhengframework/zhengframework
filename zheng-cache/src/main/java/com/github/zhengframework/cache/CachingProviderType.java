package com.github.zhengframework.cache;

/*-
 * #%L
 * zheng-cache
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

public enum CachingProviderType {
  NOOP("noop", "com.github.zhengframework.cache.noop.NoOpCachingProvider") //
  ,
  SIMPLE("simple", "com.github.zhengframework.cache.simple.SimpleCachingProvider") //
  ,
  CACHE2K("cache2k", "org.cache2k.jcache.provider.JCacheProvider") //
  ,
  EHCACHE("ehcache", "org.ehcache.jsr107.EhcacheCachingProvider") //
  ,
  INFINISPAN("infinispan", "org.infinispan.jcache.embedded.JCachingProvider") //
  ,
  CAFFEINE("caffeine", "com.github.benmanes.caffeine.jcache.spi.CaffeineCachingProvider") //
  ,
  JCS("jcs", "org.apache.commons.jcs.jcache.JCSCachingProvider") //
  ,
  HAZELCAST("hazelcast", "com.hazelcast.cache.HazelcastCachingProvider") //
  ,
  IGNITE("ignite", "org.apache.ignite.cache.CachingProvider") //
;

  private String type;
  private String className;

  CachingProviderType(String type, String className) {
    this.type = type;
    this.className = className;
  }

  public String getType() {
    return type;
  }

  public String getClassName() {
    return className;
  }
}
