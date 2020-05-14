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
