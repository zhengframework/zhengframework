# zheng framework

Zheng framework is a modular framework with many ready-made modules.

Zheng framework uses Google guice for dependency management. For ease of use, most modules support automatic loading through ServiceLoader.

## Maven Usage
```
<dependencyManagement>
    <dependencies>
      <dependency>
        <artifactId>zheng-bom</artifactId>
        <groupId>com.github.zhengframework</groupId>
        <scope>import</scope>
        <type>pom</type>
        <version>${zheng.version}</version>
      </dependency>
    </dependencies>
</dependencyManagement>

<dependencies>
    <dependency>
      <artifactId>zheng-bootstrap</artifactId>
      <groupId>com.github.zhengframework</groupId>
    </dependency>
</dependencies>
```


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
| JdbiModule | OK    | yes |
| JOOQModule | OK    | yes |
| QuerydslModule | OK    | yes |
| Sql2oModule | OK    | yes |
| CommonsDBUtilsModule | OK    | yes |
| MybatisModule | OK    | yes |
| MybatisMultiModule | OK    | no |
| MybatisXmlModule | OK    | yes |
| MybatisXmlMultiModule | OK    | no |
| HealthCheckModule | OK    | yes |
| ShiroModule | OK    | yes |
| ShiroWebModule | OK    | yes |
| ShiroJaxrsModule | OK    | yes |
| FlywayMigrateModule | Ok    | yes |
| LiquibaseMigrateModule | OK    | yes |
| EventModule | OK    | yes |
| ValidatorModule(Apache Bval) | OK    | yes |
| ValidatorModule(Hibernate) | OK    | yes |
| EbeanModule | OK    | yes |
| JobModule | OK    | yes |
| RemoteConfigModule | OK    | yes |
