## 快速入门

### 第一步：添加maven依赖
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

### 第二步：添加配置
在resource目录里创建配置文件

`src/test/resources/application.properties`

内容:
```
zheng.web.contextPath=/
zheng.web.port=8080
zheng.rest.path=/
```

### 第三步：创建REST资源

创建一个类用于展示对象注入。
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
### 第四步：创建guice模块

```
import com.google.inject.servlet.ServletModule;

public class FirstModule extends ServletModule {

  @Override
  protected void configureServlets() {
    bind(FirstResource.class);
  }
}

```

### 第五步：创建启动类

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

### 第六步：运行代码

1. 运行 `FirstUseZhengFramework.main`
2. 打开网页浏览器，输入网址: `http://127.0.0.1:8080/first/hello`
3. 你将会得到字符串: **hello, thanks to use zheng framework**
