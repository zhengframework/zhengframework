config class: com.dadazhishi.zheng.redis.RedisConfig

config example:
```
zheng.redis.host=127.0.0.1
zheng.redis.port=6379
```

```java
@Inject
RedisConfig redisConfig;

@Inject
RedisClient redisClient;
```

group redis example
```
zheng.redis.group=true
zheng.redis.a1.host=127.0.0.1
zheng.redis.a1.port=6379
zheng.redis.a2.host=127.0.0.1
zheng.redis.a2.port=6380
```
```java
@Named("a1")
@Inject
RedisConfig redisConfig1;

@Named("a1")
@Inject
RedisClient redisClient1;

@Named("a2")
@Inject
RedisConfig redisConfig2;

@Named("a2")
@Inject
RedisClient redisClient2;
```
