# zheng framework

![Java CI with Maven](https://github.com/zhengframework/zhengframework/workflows/Java%20CI%20with%20Maven/badge.svg)
[![][maven img]][maven]
[![][release img]][release]
[![][license img]][license]

[maven]:http://search.maven.org/#search|gav|1|g:"com.github.zhengframework"%20AND%20a:"zheng-core"
[maven img]:https://maven-badges.herokuapp.com/maven-central/com.github.zhengframework/zheng-bootstrap/badge.svg

[release]:https://github.com/zhengframework/zhengframework/releases
[release img]:https://img.shields.io/github/v/release/zhengframework/zhengframework.svg

[license]:LICENSE.txt
[license img]:https://img.shields.io/badge/License-Apache%202-blue.svg


zheng framework is a modular framework with many ready-to-use modules

zheng framework use google guice for DI, and most module support auto load.


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



## maven deploy

https://maven.apache.org/maven-ci-friendly.html

```
mvn -Drevision=2.0.0 -DskipTests=true clean package 
mvn -Drevision=2.0.0 clean package 

```

deploy to maven repo
```
mvn -Drevision=1.0.2 deploy -P ossrh,javadoc8
```

find dependency info
```
mvn dependency:analyze -DignoreNonCompile
mvn dependency:tree
```

