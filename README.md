# zheng framework

zheng framework is com.github.zhengframework.jpa.a modular framework with many ready-to-use modules

zheng framework use google guice for DI, and most module support auto load.


| Module      | status      | auto load   |
| ----------- | ----------- | ----------- |
| ConfigurationModule  | OK      | yes |
| JettyWebModule  | OK      | yes |
| UndertowWebModule | OK    | yes |
| WebjarsModule | OK    | yes |
| SwaggerModule | OK    | yes |
| RestModule | OK    | yes |
| RedisModule | OK    | yes |
| RabbitMQModule | OK    | yes |
| CacheModule | OK    | yes |
| MongodbModule | OK    | yes |
| MemcachedModule | OK    | yes |
| MetricsModule | OK    | yes |
| MetricsServletModule | OK    | yes |
| JpaModule(Hibernate) | OK    | yes |
| JpaMultiModule(Hibernate) | OK    | no |
| JpaModule(EclipseLink) | OK    | yes |
| JpaMultiModule(EclipseLink) | OK    | no |
| DataSourceModule | OK    | yes |
| JdbiModule | OK    | no |
| JOOQModule | OK    | no |
| QuerydslModule | OK    | no |
| MybatisModule | Waiting    |  |
| ShiroModule | Waiting    |  |
| HealthCheckModule | Waiting    |  |
| ActiveMQModule | Waiting    |  |
| ActiveMQArtemisModule | Waiting    |  |
| ActiveMQArtemisModule | Waiting    |  |
| ActiveMQArtemisModule | Waiting    |  |
| FlywayMigrateModule | Waiting    |  |
| LiquibaseMigrateModule | Waiting    |  |



## maven deploy

https://maven.apache.org/maven-ci-friendly.html

```
mvn -Drevision=2.0.0 -DskipTests=true clean package 
mvn -Drevision=2.0.0 clean package 

```

deploy to maven repo
```
mvn -Drevision=1.0.1 deploy -P ossrh,javadoc8
```

find dependency info
```
mvn dependency:analyze -DignoreNonCompile
mvn dependency:tree
```

