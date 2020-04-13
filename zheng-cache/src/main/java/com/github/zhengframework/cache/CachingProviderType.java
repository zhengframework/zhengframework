package com.github.zhengframework.cache;

public enum CachingProviderType {

  NOOP("noop", "com.github.zhengframework.cache.noop.NoOpCachingProvider") //
  , SIMPLE("simple", "com.github.zhengframework.cache.simple.SimpleCachingProvider") //
  , CACHE2K("cache2k", "org.cache2k.jcache.provider.JCacheProvider")//
  , EHCACHE("ehcache", "org.ehcache.jsr107.EhcacheCachingProvider")//
  , INFINISPAN("infinispan", "org.infinispan.jcache.embedded.JCachingProvider")//
  , CAFFEINE("caffeine", "com.github.benmanes.caffeine.jcache.spi.CaffeineCachingProvider")//
  , JCS("jcs", "org.apache.commons.jcs.jcache.JCSCachingProvider")//
  , HAZELCAST("hazelcast", "com.hazelcast.cache.HazelcastCachingProvider")//
  , IGNITE("ignite", "org.apache.ignite.cache.CachingProvider")//
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
