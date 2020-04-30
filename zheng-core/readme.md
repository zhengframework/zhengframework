## auto load module support
```
public class YourModuleProvider implements ModuleProvider{

    public Module getModule(){
        return new YourModule();
    }

}
```

META-INF/services/com.github.zhengframework.core.ModuleProvider


## Lifecycle support  
You can use `@PostConstruct` and `@PreDestroy` on the method.  
```java
public class PostConstructTest {
    @PostConstruct
    public void postConstruct() {
        // More code here 
    }
    
    @PreDestroy
    public void PreDestroy() {
        // More code here 
    }
}