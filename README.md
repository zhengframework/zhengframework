# zheng framework

please go to documentation website [zhengframework](https://zhengframework.github.io/zhengframework/)

![Java CI with Maven](https://github.com/zhengframework/zhengframework/workflows/Java%20CI%20with%20Maven/badge.svg)
[![][maven img]][maven]
[![][release img]][release]
[![][snapshot img]][snapshot]
[![][license img]][license]

[maven]:http://search.maven.org/#search|gav|1|g:"com.github.zhengframework"%20AND%20a:"zheng-core"
[maven img]:https://maven-badges.herokuapp.com/maven-central/com.github.zhengframework/zheng-bootstrap/badge.svg

[snapshot]:https://github.com/zhengframework/zhengframework
[snapshot img]:https://img.shields.io/nexus/s/com.github.zhengframework/zheng-core?server=https%3A%2F%2Foss.sonatype.org

[release]:https://github.com/zhengframework/zhengframework/releases
[release img]:https://img.shields.io/github/v/release/zhengframework/zhengframework.svg

[license]:LICENSE.txt
[license img]:https://img.shields.io/badge/License-Apache%202-blue.svg

Zheng framework is a modular framework with many ready-made modules.

Zheng framework uses Google guice for dependency management. For ease of use, most modules support automatic loading through ServiceLoader.

## Quick Start

### step 1: add maven dependency
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
    <dependency>
      <artifactId>zheng-web-jetty</artifactId>
      <groupId>com.github.zhengframework</groupId>
    </dependency>
    <dependency>
      <artifactId>zheng-rest</artifactId>
      <groupId>com.github.zhengframework</groupId>
    </dependency>
</dependencies>
```

### step 2: add config
create config file in resource dir:

`src/test/resources/application.properties`

content:
```
zheng.web.contextPath=/
zheng.web.port=8080
zheng.rest.path=/
```

### step 3: create rest resource

create a class to show inject object.
```
public class Man {

  public String say() {
    return "hello, thanks to use zheng framework";
  }
}
```

```
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("first")
public class FirstResource {
  private final Man man;

  @Inject
  public FirstResource(Man man) {
    this.man = man;
  }
  
  @GET
  @Path("hello")
  public String hello() {
    return man.say();
  }
}
```
### step 4: create guice module

```
import com.google.inject.servlet.ServletModule;

public class FirstModule extends ServletModule {

  @Override
  protected void configureServlets() {
    bind(FirstResource.class);
  }
}

```

### step 5: create bootstrap class

```
import com.github.zhengframework.bootstrap.ZhengApplication;
import com.github.zhengframework.bootstrap.ZhengApplicationBuilder;

public class FirstUseZhengFramework {

  public static void main(String[] args) throws Exception {
    ZhengApplication application = ZhengApplicationBuilder.create()
        .addModule(new FirstModule())
        .build();
    application.start();
  }
}
```

### step 6: run code

1. please run `FirstUseZhengFramework.main`
2. open your web browser, enter url: `http://127.0.0.1:8080/first/hello`
3. you will get string: **hello, thanks to use zheng framework**


### Module Info

| Module      | status      | auto load   |
| ----------- | ----------- | ----------- |
| zheng-configuration | OK | yes |
| zheng-web-jetty | OK | yes |
| zheng-web-undertow | OK | yes |
| zheng-webjars | OK | yes |
| zheng-swagger | OK | yes |
| zheng-rabbitmq | OK | yes |
| zheng-healthcheck | OK | yes |
| zheng-cache | OK | yes |
| zheng-mongodb | OK | yes |
| zheng-memcached | OK | yes |
| zheng-redis | OK | yes |
| zheng-metrics | OK | yes |
| zheng-metrics-servlet | OK | yes |
| zheng-rest | OK | yes |
| zheng-shiro | OK | yes |
| zheng-shiro-web | OK | yes |
| zheng-shiro-jaxrs | OK | yes |
| zheng-log-logback | OK | yes |
| zheng-jdbc | OK | yes |
| zheng-migrate-flyway | OK | yes |
| zheng-migrate-liquibase | OK | yes |
| zheng-jdbc-jooq | OK | yes |
| zheng-jdbc-querydsl | OK | yes |
| zheng-jdbc-jdbi | OK | yes |
| zheng-jdbc-sql2o | OK | yes |
| zheng-jdbc-commons-dbutils | OK | yes |
| zheng-jdbc-ebean | OK | yes |
| zheng-jpa-hibernate | OK | yes |
| zheng-jpa-eclipselink | OK | yes |
| zheng-mybatis | OK | yes |
| zheng-event-guava | OK | yes |
| zheng-validator-hibernate | OK | yes |
| zheng-validator-bval | OK | yes |
| zheng-job | OK | yes |
| zheng-remote-config | OK | yes |

