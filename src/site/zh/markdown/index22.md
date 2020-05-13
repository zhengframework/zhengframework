# zheng framework中文文档

zheng framework 是一个具有许多现成的模块的模块化框架。

zheng framework 通过google guice进行依赖管理。为方便使用，大多数模块都支持通过ServiceLoader自动加载。

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

| 模块      | 完成状态      | 是否支持自动加载   |
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
